package com.zzvc.mmps.data.importer.file.csv;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.zzvc.mmps.data.importer.file.FileRwException;
import com.zzvc.mmps.data.importer.file.FileWriter;

public class CsvFileWriter implements FileWriter {
	private static Logger logger = Logger.getLogger(CsvFileWriter.class);
	
	private ByteArrayOutputStream os;
	
	private BufferedWriter bw;
	private boolean newLine = true;
	
	public CsvFileWriter() {
		try {
			os = new ByteArrayOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		}
	}

	@Override
	public void write(File file) {
		try {
			bw.close();
			
			OutputStream fileOs = new FileOutputStream(file); 
			os.writeTo(fileOs);
			os.close();
			fileOs.close();
		} catch (FileNotFoundException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		} catch (IOException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		}
	}

	@Override
	public void newRow(int colId) {
		try {
			if (!newLine) {
				bw.newLine();
				newLine = true;
			}
			for (int i = 1; i < colId; i++) {
				addValue("");
			}
		} catch (IOException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		}
	}

	@Override
	public void addValue(String value) {
		try {
			if (newLine) {
				newLine = false;
			} else {
				bw.append(",");
			}
			bw.append(value);
		} catch (IOException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		}
		
	}

}
