package com.zzvc.mmps.data.importer.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.zzvc.mmps.console.ConsoleMessageSupport;
import com.zzvc.mmps.data.importer.file.FileReader;
import com.zzvc.mmps.data.importer.file.FileRwException;
import com.zzvc.mmps.data.importer.file.FileRwFactory;
import com.zzvc.mmps.data.importer.file.FileWriter;
import com.zzvc.mmps.data.importer.file.InvalidValueException;
import com.zzvc.mmps.data.importer.service.PlayerDataException;
import com.zzvc.mmps.data.importer.service.PlayerDataManager;
import com.zzvc.mmps.data.importer.service.PlayerIOManager;
import com.zzvc.mmps.model.Player;

public class PlayerIOManagerImpl extends ConsoleMessageSupport implements PlayerIOManager {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssS'.xls'");
	
	@Autowired
	private FileRwFactory fileRwFactory;
	
	@Resource
	private PlayerDataManager playerDataManager;

	public PlayerIOManagerImpl() {
		super();
		pushBundle("PlayerImporterResources");
	}
	
	@Override
	public String getConsolePrefix() {
		return "";
	}

	/* (non-Javadoc)
	 * @see com.zzvc.mmps.data.importer.service.impl.PlayerIOManager#importPlayer(java.io.File)
	 */
	@Override
	public void importPlayer(File file) {
		if (!file.exists()) {
			warnMessage("player.importer.warn.file.notexists", file.getAbsolutePath());
			return;
		}
		
		try {
			infoMessage("player.importer.import.reading", file.getAbsolutePath());
			FileReader reader = fileRwFactory.createFileReader();
			reader.read(file);
			List<Player> players = playerDataManager.readPlayers(reader);

			infoMessage("player.importer.import.backup");
			FileWriter writer = fileRwFactory.createFileWriter();
			playerDataManager.exportPlayer(writer);
			writer.write(getBackupFile());
			
			infoMessage("player.importer.import.importing");
			playerDataManager.importPlayer(players);
			infoMessage("player.importer.import.importing.done");
			
			info(getPlayerSummary(players));
		} catch (InvalidValueException e) {
			errorMessage("player.importer.error.file.invalidvalue", e.getRowId(), e.getColId());
		} catch (PlayerDataException e) {
			errorMessage(e.getMessageKey());
		} catch (FileRwException e) {
			errorMessage(e.getMessageKey());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.zzvc.mmps.data.importer.service.impl.PlayerIOManager#exportPlayer(java.io.File)
	 */
	@Override
	public void exportPlayer(File file) {
		if (file.exists()) {
			warnMessage("player.importer.warn.file.alreadyexists", file.getAbsolutePath());
			return;
		}
		if (!file.getParentFile().exists()) {
			warnMessage("player.importer.warn.file.foldernotexists", file.getParentFile().getAbsolutePath());
			return;
		}
		
		try {
			infoMessage("player.importer.export.exporting", file.getAbsolutePath());
			FileWriter writer = fileRwFactory.createFileWriter();
			playerDataManager.exportPlayer(writer);
			writer.write(file);
			infoMessage("player.importer.export.exporting.done");
		} catch (PlayerDataException e) {
			errorMessage(e.getMessageKey());
		} catch (FileRwException e) {
			errorMessage(e.getMessageKey());
		}
	}
	
	public String getFileExt() {
		return fileRwFactory.getFileExt();
	}
	
	private File getBackupFile() {
		return new File(format.format(new Date()));
	}
	
	private String getPlayerSummary(List<Player> players) {
		StringBuffer sb = new StringBuffer();
		sb.append(findText("player.importer.import.player")).append("\n");
		for (Player player : players) {
			sb.append(findText("player.importer.import.player.info", player.getName(), player.getAddress()));
			sb.append("\n");
		}
		sb.append(findText("player.importer.import.player.count", players.size()));
		return sb.toString();
	}

}
