package utils;

import java.io.File;
import java.time.Duration;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "AutoTester", 
version = "1", 
description = "Tests a C program. Using .in as the entry value and .out as the output. Checking if the program entry matches with the required output provided")
public class CliOptions {

	@Option(names = { "--help", "-h", "-help",
			"--h" }, usageHelp = true, description = "Display the Help or How the application works.")
	private boolean help;

	@Option(names = { "-v", "--v", "--version",
			"-version" }, versionHelp = true, description = "Display the actual version of the application.")
	private boolean version;

	@Option(names = { "-e", "--e", "--executable",
			"-executable" }, required = true, description = "Path to the C or CPP application executable / Can be left empty and it will fetch directly from the folder name.")
	private String cExecutable;

	@Parameters(index = "0", arity = "0..1")
	private String cExecutablePositional;

	@Option(names = { "--td", "-td", "--tdir", "-tdir" }, required = false, description = "Path for the test folder.")
	private File testDir;

	@Parameters(index = "1", arity = "0..1")
	private File testDirPositional;

	@Option(names = { "--time", "-time", "-tm",
			"--tm" }, converter = DurationConverter.class, required = false, defaultValue = "5s", description = "Set a custom max time to the test run || Can be set a custom timeout duration (e.g. 5s, 2m, 1h, or raw seconds)")
	private Duration timeLimit;

	public String getcExecutable() {
		if (cExecutable != null && !cExecutable.isBlank()) {
			return cExecutable;
		}
		if (cExecutablePositional != null && !cExecutablePositional.isBlank()) {
			return cExecutablePositional;
		}

		String data = new File(".").getAbsoluteFile().getName();
		return (data != null && !data.isBlank() ? data.contains("/") || data.startsWith(".") ? data : "./" + data
				: "./");
	}

	public File getTestDir() {
		if (testDir != null) {
			return testDir;
		}
		if (testDirPositional != null) {
			return testDirPositional;
		}
		File defaultTests = new File("tests");
		if (defaultTests.exists() && defaultTests.isDirectory()) {
			return defaultTests;
		}
		return new File(".");
	}

	public Duration getTimeLimit() {
		return timeLimit;
	}

}
