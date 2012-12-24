package com.zzvc.mmps.data.importer.file.jxl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.zzvc.mmps.data.importer.file.FileRwException;
import com.zzvc.mmps.data.importer.file.FileWriter;

public class JxlFileWriter implements FileWriter {
	private static Logger logger = Logger.getLogger(JxlFileWriter.class);
	
	private ByteArrayOutputStream os = new ByteArrayOutputStream();
	
	private WritableWorkbook workbook;
	private WritableSheet sheet;
	
	private int rowId = 0;
	private int colId = 0;

	public JxlFileWriter() {
		workbook = createWritableWorkbook();
		sheet = workbook.createSheet("Sheet1", 0);
	}
	
	@Override
	public void write(File file) {
		try {
			workbook.write();
			workbook.close();
			
			os.writeTo(new FileOutputStream(file));
		} catch (WriteException e) {
			logger.error("Cannot update Excel file.", e);
			throw new FileRwException("player.importer.error.file.write");
		} catch (FileNotFoundException e) {
			logger.error("Cannot write CSV file", e);
			throw new FileRwException("player.importer.error.file.write");
		} catch (IOException e) {
			logger.error("Cannot update Excel file.", e);
			throw new FileRwException("player.importer.error.file.write");
		}
	}

	@Override
	public void newRow(int colId) {
		rowId++;
		this.colId = colId;
	}
	
	@Override
	public void addValue(String value) {
		try {
			sheet.addCell(new Label(colId - 1, rowId - 1, value));
		} catch (RowsExceededException e) {
			logger.error("Cannot update Excel file.", e);
			throw new FileRwException("player.importer.error.file.write");
		} catch (WriteException e) {
			logger.error("Cannot update Excel file.", e);
			throw new FileRwException("player.importer.error.file.write");
		}
		colId++;
	}
	
	private WritableWorkbook createWritableWorkbook() {
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
		} catch (IOException e) {
			logger.error("Cannot access Excel file.", e);
			throw new FileRwException("player.importer.error.file.write");
		}
		return workbook;
	}
	
}
