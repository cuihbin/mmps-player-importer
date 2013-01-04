package com.zzvc.mmps.data.importer;

import java.io.File;

import javax.annotation.Resource;

import com.zzvc.mmps.data.importer.service.PlayerIOManager;
import com.zzvc.mmps.gui.console.GuiConsole;
import com.zzvc.mmps.gui.console.GuiConsoleButton;
import com.zzvc.mmps.gui.file.FileException;

public class PlayerImporterGuiConsole extends GuiConsole {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private PlayerIOManager playerIOManager;
	
	public PlayerImporterGuiConsole() {
		super();
		pushBundle("PlayerImporterGuiResources");
		
		addButton(new GuiConsoleButton(findText("player.importer.gui.button.import")) {
			@Override
			public void buttonAction() {
				try {
					File file = fileChooser.selectFileForRead(playerIOManager.getFileExt(), findText("player.importer.gui.file.filter.description." + playerIOManager.getFileExt()));
					playerIOManager.importPlayer(file);
				} catch (FileException e) {
				}
			}
		});
		
		addButton(new GuiConsoleButton(findText("player.importer.gui.button.export")) {
			@Override
			public void buttonAction() {
				try {
					File file = fileChooser.selectFileForWrite(playerIOManager.getFileExt(), findText("player.importer.gui.file.filter.description." + playerIOManager.getFileExt()));
					playerIOManager.exportPlayer(file);
				} catch (FileException e) {
				}
			}
		});
	}
}
