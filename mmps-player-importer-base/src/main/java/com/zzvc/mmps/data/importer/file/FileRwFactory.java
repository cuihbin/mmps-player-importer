package com.zzvc.mmps.data.importer.file;

public interface FileRwFactory {
	FileReader createFileReader();
	FileWriter createFileWriter();
	String getFileExt();
}
