package com.zzvc.mmps.data.importer.service;

import java.io.File;

public interface PlayerIOManager {

	void importPlayer(File file);

	void exportPlayer(File file);
	
	String getFileExt();

}