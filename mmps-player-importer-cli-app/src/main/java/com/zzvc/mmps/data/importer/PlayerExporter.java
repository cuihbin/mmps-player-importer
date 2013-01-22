package com.zzvc.mmps.data.importer;

import java.io.File;

public class PlayerExporter extends PlayerIoBase {
	
	@Override
	protected String getCommandType() {
		return "export";
	}

	@Override
	protected void executeCommand(File file) {
		getPlayerIoManager().exportPlayer(file);
	}

	public static void main(String[] args) {
		PlayerExporter exporter = new PlayerExporter();
		exporter.execute(args);
	}

}
