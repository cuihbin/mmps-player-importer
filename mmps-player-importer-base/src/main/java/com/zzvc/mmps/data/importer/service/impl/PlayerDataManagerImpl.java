package com.zzvc.mmps.data.importer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.data.importer.file.FileReader;
import com.zzvc.mmps.data.importer.file.FileWriter;
import com.zzvc.mmps.data.importer.file.InvalidValueException;
import com.zzvc.mmps.data.importer.service.PlayerDataManager;
import com.zzvc.mmps.model.Player;
import com.zzvc.mmps.model.PlayerKey;
import com.zzvc.mmps.model.PlayerKeyValue;

public class PlayerDataManagerImpl implements PlayerDataManager {
	private static Logger logger = Logger.getLogger(PlayerDataManagerImpl.class);
	
	@Resource
	private PlayerDao playerDao;

	@Override
	public void importPlayer(List<Player> players) {
		Map<String, Date> lastHeartbeat = clearPlayersAndReserveLastHeartbeat();
		restoreLastHeartbeat(players, lastHeartbeat);
		for (Player player : players) {
			playerDao.save(player);
		}
	}

	@Override
	public void exportPlayer(FileWriter writer) {
		exportPlayers(findPlayers(), writer);
	}
	
	@Override
	public List<Player> readPlayers(FileReader reader) {
		
		reader.nextRow();
		
		Set<PlayerKey> publicKeys = readPlayerKeys(reader, 1);
		
		List<Player> players = new ArrayList<Player>();
		while (!reader.isEof()) {
			Player player = readPlayer(reader);
			mergePlayerKeys(publicKeys, player.getPlayerKeies());
			players.add(player);
		}
		
		return players;
	}
	
	private Player readPlayer(FileReader reader) {
		if (reader.getRowIndent() != 0) {
			throw new InvalidValueException(reader.getRowId(), 1);
		}
		if (reader.getRowLength() < 2) {
			throw new InvalidValueException(reader.getRowId(), 2);
		}
		
		String[] rowValues = reader.getRowValues();
		Player player = new Player();
		player.setName(rowValues[0]);
		player.setAddress(rowValues[1]);
		player.setEnabled(true);
		
		reader.nextRow();
		player.setPlayerKeies(readPlayerKeys(reader, 1));

		return player;
	}
	
	private Set<PlayerKey> readPlayerKeys(FileReader reader, int keyLevel) {
		Set<PlayerKey> playerKeys = new TreeSet<PlayerKey>();
		while (!(reader.isEof() || reader.getRowIndent() != keyLevel)) {
			PlayerKey playerKey = readPlayerKey(reader, keyLevel);
			playerKeys.add(playerKey);
		}
		return playerKeys;
	}

	private PlayerKey readPlayerKey(FileReader reader, int keyLevel) {
		if (keyLevel != reader.getRowIndent()) {
			throw new InvalidValueException(reader.getRowId(), keyLevel + 1);
		}
		if (reader.getRowLength() < keyLevel + 2) {
			throw new InvalidValueException(reader.getRowId(), keyLevel + 2);
		}

		String[] rowValues = reader.getRowValues();
		PlayerKey playerKey = new PlayerKey();
		playerKey.setKeyName(rowValues[0]);
		Set<PlayerKeyValue> playerKeyValues = new TreeSet<PlayerKeyValue>();
		for (int i = 1; i < rowValues.length; i++) {
			PlayerKeyValue playerKeyValue = new PlayerKeyValue();
			playerKeyValue.setValue(rowValues[i]);
			playerKeyValue.setPlayerKey(playerKey);
			
			playerKeyValues.add(playerKeyValue);
		}
		playerKey.setPlayerKeyValues(playerKeyValues);

		reader.nextRow();
		playerKey.setPlayerKeies(readPlayerKeys(reader, keyLevel + 1));
		
		return playerKey;
	}
	
	private Map<String, Date> clearPlayersAndReserveLastHeartbeat() {
		Map<String, Date> lastHeartbeatTime = new HashMap<String, Date>();
		for (Player player : playerDao.getAll()) {
			lastHeartbeatTime.put(player.getAddress(), player.getLastHeartbeat());
			playerDao.remove(player.getId());
		}
		return lastHeartbeatTime;
	}
	
	private void restoreLastHeartbeat(List<Player> players, Map<String, Date> lastHeartbeat) {
		for (Player player : players) {
			if (lastHeartbeat.containsKey(player.getAddress())) {
				player.setLastHeartbeat(lastHeartbeat.get(player.getAddress()));
			}
		}
	}
	
	private void mergePlayerKeys(Set<PlayerKey> from, Set<PlayerKey> to) {
		Map<String, PlayerKey> playerKeyMap = new HashMap<String, PlayerKey>();
		
		for (PlayerKey publicKey : to) {
			playerKeyMap.put(publicKey.getKeyName(), publicKey);
		}
		
		for (PlayerKey playerKey : from) {
			if (!playerKeyMap.containsKey(playerKey.getKeyName())) {
				to.add(playerKey);
			}
		}
	}
	
	private List<Player> findPlayers() {
		return playerDao.getAll();
	}
	
	private void exportPlayers(List<Player> players, FileWriter writer) {
		for (Player player : findPlayers()) {
			exportPlayer(player, writer);
		}
	}
	
	private void exportPlayer(Player player, FileWriter writer) {
		writer.newRow(1);
		writer.addValue(player.getName());
		writer.addValue(player.getAddress());
		
		exportPlayerKeys(player.getPlayerKeies(), 1, writer);
	}
	
	private void exportPlayerKeys(Set<PlayerKey> playerKeys, int keyLevel, FileWriter writer) {
		for (PlayerKey playerKey : playerKeys) {
			exportPlayerKey(playerKey, keyLevel, writer);
		}
	}
	
	private void exportPlayerKey(PlayerKey playerKey, int keyLevel, FileWriter writer) {
		writer.newRow(keyLevel + 1);
		writer.addValue(playerKey.getKeyName());
		for (PlayerKeyValue playerKeyValue : playerKey.getPlayerKeyValues()) {
			writer.addValue(playerKeyValue.getValue());
		}
		exportPlayerKeys(playerKey.getPlayerKeies(), keyLevel + 1, writer);
	}
	
}
