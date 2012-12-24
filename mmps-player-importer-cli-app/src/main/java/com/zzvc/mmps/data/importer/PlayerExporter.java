package com.zzvc.mmps.data.importer;

import java.io.File;

public class PlayerExporter extends PlayerIOBase {
	
	@Override
	protected String getCommandType() {
		return "export";
	}

	@Override
	protected void executeCommand(File file) {
		getApp().exportPlayer(file);
	}

	public static void main(String[] args) {
		PlayerExporter exporter = new PlayerExporter();
		exporter.execute(args);
	}

}
