package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLockInterruptionException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileReader {

	public File read(String input) {
		try {
			File dir = new File(input);
			if (!dir.exists()) {
				throw new FileNotFoundException();
			}
			return dir;
		} catch (FileNotFoundException ex) {
			log.error("ERROR: -> Directory not found :: %s, because of :>: ".formatted(input), ex.getCause());
			return null;
		}
	}
	
	public String readStr(File input) {
		try {
			return Files.readString(input.toPath());
		} catch (IOException ex) {
			log.error("ERROR: -> Directory not found :: %s, because of :>: ".formatted(input), ex.getCause());
			return null;
		}
	}
	
	public String readRes(String input) {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(input)) {
            if (inputStream == null) {
            	throw new FileNotFoundException();
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch(IOException ex) {
			log.error("ERROR: -> Unable to read the file :: %s, because of :>: ".formatted(input), ex.getCause());
			return null;
		}
	}

	public void write(String input, byte[] data) {
		try {
			Files.write(Path.of(input + ".data"), data);

		} catch (FileLockInterruptionException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(input), ex.getCause());
		} catch (FileAlreadyExistsException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(input), ex.getCause());
		} catch (IOException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(input), ex.getCause());
		}
	}

	public void write(byte[] data) {
		String filename = generateRandomName() + ".data";
		try {
			Files.write(Path.of(filename), data);

		} catch (FileLockInterruptionException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(filename),
					ex.getCause());
		} catch (FileAlreadyExistsException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(filename),
					ex.getCause());
		} catch (IOException ex) {
			log.error("ERROR: -> Tool was unable to write file :: %s, because of :>: ".formatted(filename),
					ex.getCause());
		}
	}

	public String generateRandomName() {
		return generateRandomName(null);
	}

	public String generateRandomName(Integer size) {
		Random random = new Random();

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int nameLength = (size != null) ? size : 8;
		StringBuilder nameBuilder = new StringBuilder(nameLength);

		for (int i = 0; i < nameLength; i++) {
			int index = random.nextInt(characters.length());
			nameBuilder.append(characters.charAt(index));
		}
		String randomName = nameBuilder.toString();

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		return randomName + timestamp;
	}
}
