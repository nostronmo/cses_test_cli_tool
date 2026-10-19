package utils;

import java.time.Duration;

import picocli.CommandLine.ITypeConverter;

public class DurationConverter implements ITypeConverter<Duration> {

	@Override
	public Duration convert(String value) throws Exception {
		value = value.toLowerCase().trim();

		if (value.endsWith("s")) {
			long seconds = Long.parseLong(value.substring(0, value.length() - 1));
			return Duration.ofSeconds(seconds);
		} else if (value.endsWith("m")) {
			long minutes = Long.parseLong(value.substring(0, value.length() - 1));
			return Duration.ofMinutes(minutes);
		} else if (value.endsWith("h")) {
			long hours = Long.parseLong(value.substring(0, value.length() - 1));
			return Duration.ofHours(hours);
		} else {
			return Duration.ofSeconds(Long.parseLong(value));
		}
	}

}
