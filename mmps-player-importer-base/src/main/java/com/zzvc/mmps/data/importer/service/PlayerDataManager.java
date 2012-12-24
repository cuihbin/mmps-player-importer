package com.zzvc.mmps.data.importer.service;

import java.util.List;

import com.zzvc.mmps.data.importer.file.FileReader;
import com.zzvc.mmps.data.importer.file.FileWriter;
import com.zzvc.mmps.model.Player;

public interface PlayerDataManager {
	List<Player> readPlayers(FileReader reader);
	void importPlayer(List<Player> players);
	void exportPlayer(FileWriter writer);
}
