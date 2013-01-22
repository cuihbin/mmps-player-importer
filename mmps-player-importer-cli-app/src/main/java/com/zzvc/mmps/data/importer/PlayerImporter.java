package com.zzvc.mmps.data.importer;

import java.io.File;

public class PlayerImporter extends PlayerIoBase {
	
	@Override
	protected String getCommandType() {
		return "import";
	}

	@Override
	protected void executeCommand(File file) {
		getPlayerIoManager().importPlayer(file);
	}

	public static void main(String[] args) {
		PlayerImporter importer = new PlayerImporter();
		importer.execute(args);
	}

}
