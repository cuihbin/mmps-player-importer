package com.zzvc.mmps.data.importer.file;

import java.io.File;

public interface FileWriter {

	void write(File file);

	void newRow(int colId);

	void addValue(String value);

}