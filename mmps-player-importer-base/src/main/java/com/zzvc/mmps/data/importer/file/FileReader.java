package com.zzvc.mmps.data.importer.file;

import java.io.File;

public interface FileReader {
	
	void read(File file);

	void nextRow();

	String[] getRowValues();

	int getRowIndent();

	int getRowLength();

	boolean isEof();

	int getRowId();

}