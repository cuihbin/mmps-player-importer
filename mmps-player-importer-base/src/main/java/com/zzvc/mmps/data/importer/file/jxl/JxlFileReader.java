package com.zzvc.mmps.data.importer.file.jxl;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;

import com.zzvc.mmps.data.importer.file.BaseFileReader;
import com.zzvc.mmps.data.importer.file.FileRwException;

public class JxlFileReader extends BaseFileReader {
	private static Logger logger = Logger.getLogger(JxlFileReader.class);
	
	private Sheet sheet;
	private Cell[] row;
	
	public void read(File file) {
		this.sheet = createWorkbookForRead(file).getSheet(0);
	}
	
	@Override
	protected boolean readNextRow() {
		rowId++;
		if (rowId <= sheet.getRows()) {
			row = sheet.getRow(rowId - 1);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected String findCellValue(int colId) {
		Cell c = row[colId - 1];
		return c.getContents().replace("　", " ").trim().replace("'", "''");
	}
	
	@Override
	protected int findRawDataLength() {
		return row.length;
	}

	private Workbook createWorkbookForRead(File file) {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(file);
		} catch (IOException e) {
			logger.error("Cannot read Excel file.", e);
			throw new FileRwException("player.importer.error.file.read");
		} catch (BiffException e) {
			logger.error("Cannot parse Excel file.", e);
			throw new FileRwException("player.importer.error.file.read");
		}
		return workbook;
	}
}
