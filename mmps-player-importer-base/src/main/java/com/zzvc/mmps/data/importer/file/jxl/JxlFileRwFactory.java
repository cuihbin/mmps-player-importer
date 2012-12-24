package com.zzvc.mmps.data.importer.file.jxl;

import com.zzvc.mmps.data.importer.file.FileReader;
import com.zzvc.mmps.data.importer.file.FileRwFactory;
import com.zzvc.mmps.data.importer.file.FileWriter;

public class JxlFileRwFactory implements FileRwFactory {

	@Override
	public FileReader createFileReader() {
		return new JxlFileReader();
	}

	@Override
	public FileWriter createFileWriter() {
		return new JxlFileWriter();
	}

	@Override
	public String getFileExt() {
		return "xls";
	}

}
