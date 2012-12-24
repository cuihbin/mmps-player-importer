package com.zzvc.mmps.data.importer.file;

public class FileRwException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String messageKey;

	public FileRwException(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
