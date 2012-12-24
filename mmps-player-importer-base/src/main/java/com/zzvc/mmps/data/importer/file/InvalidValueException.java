package com.zzvc.mmps.data.importer.file;

public class InvalidValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private int rowId;
	private int colId;
	
	public InvalidValueException(int rowId, int colId) {
		this.rowId = rowId;
		this.colId = colId;
	}

	public int getRowId() {
		return rowId;
	}

	public int getColId() {
		return colId;
	}
}
