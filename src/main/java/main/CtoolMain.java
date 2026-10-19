package main;

import logic.TesterTool;
import picocli.CommandLine;

public class CtoolMain {

	public static void main(String[] args) {
		int exitCode = new CommandLine(new TesterTool()).execute(args);
		System.exit(exitCode);
	}

}
