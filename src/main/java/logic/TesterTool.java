package logic;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import utils.CliOptions;
import utils.FileReader;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;

@Slf4j
public class TesterTool extends CliOptions implements Callable<Integer> {

	private FileReader fileReader = new FileReader();

	public void showTestVars() {
		String template = fileReader.readRes("tool_start.md");
		String execName = getcExecutable();
		String folderPath = getTestDir().getAbsolutePath();

		if (template != null) {
			String banner = template.formatted(execName, folderPath);
			log.info("\n%s".formatted(banner));
		}
	}

	public void runTest(File testDir) {
		File[] inFiles = testDir.listFiles((d, name) -> name.endsWith(".in"));
		if (inFiles == null || inFiles.length == 0) {
			log.warn("No test files found in folder: %s".formatted(testDir.getPath()));
			return;
		}

		Arrays.sort(inFiles, Comparator.comparing(File::getName));
		int passed = 0, failed = 0, errors = 0;
		Duration timeout = (getTimeLimit() != null) ? getTimeLimit() : Duration.ofSeconds(5);

		for (File inFile : inFiles) {
			String fileName = inFile.getName();
			String testName = fileName.substring(0, fileName.length() - 3);

			File outFile = new File(testDir, testName + ".out");

			if (!outFile.exists()) {
				log.warn("Test :: [{}]: :>: ISSUE :: (.out file missing, skipping test.)", testName);
				errors++;
				continue;
			}

			try {
				ProcessBuilder pb = new ProcessBuilder(getcExecutable());
				pb.redirectInput(inFile);
				Process process = pb.start();

				String actualOutput;
				try (InputStream is = process.getInputStream()) {
					actualOutput = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				}

				boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
				if (!finished) {
					process.destroy();
					log.warn("Test :: [{}] :>: {}", testName, ansi().fg(YELLOW).bold().a("TIMEOUT (>" + timeout + ")").reset());
					failed++;
					continue;
				}

				String expectedOutput = Files.readString(outFile.toPath());

				if (compareStrings(expectedOutput, actualOutput)) {
					log.info("Test :: [{}] :>: {}", testName, ansi().fg(GREEN).bold().a("PASSED").reset());
					passed++;
				} else {
					log.error("Test :: [{}] :>: {}", testName, ansi().fg(RED).bold().a("FAILED").reset());
					failed++;
				}
			} catch (Exception e) {
				log.error("Test :: [{}] :>: {}", testName, ansi().fg(RED).bold().a("ERROR (" + e.getMessage() + ")").reset());
				errors++;
			}
		}

		String template = fileReader.readRes("tool_response.md");
		if (template != null) {
			String banner = template.formatted(passed, failed, errors);
			log.info("\n%s".formatted(banner));
		}
	}

	private static boolean compareStrings(String expected, String actual) {
		String value1 = expected.trim().replaceAll("\\s+", " ");
		String value2 = actual.trim().replaceAll("\\s+", " ");
		return value1.equals(value2);
	}

	@Override
	public Integer call() throws Exception {
		showTestVars();
		runTest(getTestDir());
		return 0;
	}

}
