package com.zzvc.mmps.data.importer.file.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.zzvc.mmps.data.importer.file.BaseFileReader;
import com.zzvc.mmps.data.importer.file.FileRwException;

public class CsvFileReader extends BaseFileReader {
	private static Logger logger = Logger.getLogger(CsvFileReader.class);
	
	private BufferedReader br = null;
	private String[] row;

	public void read(File file) {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (FileNotFoundException e) {
			logger.error("Cannot read CSV file", e);
			throw new FileRwException("player.importer.error.file.read");
		} catch (UnsupportedEncodingException e) {
			logger.error("Cannot read CSV file", e);
			throw new FileRwException("player.importer.error.file.read");
		}
	}
	
	@Override
	protected boolean readNextRow() {
		try {
			String line = br.readLine();
			if (line == null) {
				return false;
			}
			row = line.split(",");
			return true;
		} catch (IOException e) {
			logger.error("Cannot read CSV file", e);
			throw new FileRwException("player.importer.error.file.read");
		}
	}

	@Override
	protected String findCellValue(int colId) {
		return row[colId - 1];
	}

	@Override
	protected int findRawDataLength() {
		return row.length;
	}

}
