package com.zzvc.mmps.data.importer.file.csv;

import com.zzvc.mmps.data.importer.file.FileReader;
import com.zzvc.mmps.data.importer.file.FileRwFactory;
import com.zzvc.mmps.data.importer.file.FileWriter;

public class CsvFileRwFactory implements FileRwFactory {

	@Override
	public FileReader createFileReader() {
		return new CsvFileReader();
	}

	@Override
	public FileWriter createFileWriter() {
		return new CsvFileWriter();
	}

	@Override
	public String getFileExt() {
		return "csv";
	}

}
