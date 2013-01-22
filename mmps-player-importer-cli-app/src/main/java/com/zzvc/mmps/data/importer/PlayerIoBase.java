package com.zzvc.mmps.data.importer;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.zzvc.mmps.app.util.BeanFactory;
import com.zzvc.mmps.console.ConsoleMessageSupport;
import com.zzvc.mmps.data.importer.service.PlayerIOManager;

abstract public class PlayerIoBase extends ConsoleMessageSupport {
	abstract protected String getCommandType();
	abstract protected void executeCommand(File file);
	
	public PlayerIoBase() {
		super();
		pushBundle("PlayerImporterCliResources");
	}
	
	@Override
	public String getConsolePrefix() {
		return "";
	}
	
	protected void execute(String[] args) {
		CommandLineParser parser = new PosixParser();
		Options option = getOptionsForParser();
		try {
			CommandLine line = parser.parse(option, args);
			executeCommand(new File(line.getOptionValue("file")));
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(findText("player.importer.cli.command." + getCommandType()), option);
		}
	}
	
	protected Options getOptionsForParser() {
		Options options = new Options();
		return options.addOption(OptionBuilder
				.withLongOpt("file")
				.withDescription(findText("player.importer.cli.file." + getCommandType()))
				.hasArg()
				.withArgName("FILE")
				.isRequired()
				.create("f"));
	}
	
	protected PlayerIOManager getPlayerIoManager() {
		return (PlayerIOManager) BeanFactory.getBean("playerIOManager");
	}
}
