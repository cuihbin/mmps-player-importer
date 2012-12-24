package com.zzvc.mmps.data.importer.file;

import org.apache.commons.lang.StringUtils;


abstract public class BaseFileReader implements FileReader {
	protected int rowId = 0;
	protected boolean eof = false;
	protected int rowIndent;
	protected int rowLength;
	protected String[] rowValues;
	
	abstract protected boolean readNextRow();
	abstract protected String findCellValue(int colId);
	abstract protected int findRawDataLength();

	@Override
	public void nextRow() {
		while (true) {
			try {
				if (readNextRow()) {
					rowIndent = findRowIndent();
					rowLength = findRowLength();
					rowValues = findRowValues(rowIndent, rowLength);
				} else {
					eof = true;
				}
				break;
			} catch (EmptyRowException e) {
			}
		}
	}
	
	@Override
	public String[] getRowValues() {
		return rowValues;
	}
	
	@Override
	public int getRowIndent() {
		return rowIndent;
	}
	
	@Override
	public int getRowLength() {
		return rowLength;
	}

	@Override
	public boolean isEof() {
		return eof;
	}

	@Override
	public int getRowId() {
		return rowId;
	}

	protected String[] findRowValues(int rowLevel, int rowLength) {
		String[] rowValues = new String[rowLength - rowLevel];
		for (int i = rowLevel + 1; i <= rowLength; i++) {
			rowValues[i - rowLevel - 1] = findCellValue(i);
		}
		return rowValues;
	}
	
	protected int findRowIndent() {
		for (int i = 1; i <= findRawDataLength(); i++) {
			if (!StringUtils.isBlank(findCellValue(i))) {
				return i - 1;
			}
		}
		throw new EmptyRowException();
	}
	
	protected int findRowLength() {
		for (int i = findRawDataLength(); i > 0; i--) {
			if (!StringUtils.isBlank(findCellValue(i))) {
				return i;
			}
		}
		throw new EmptyRowException();
	}

}
