package com.zzvc.mmps.data.importer.service;

public class PlayerDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String messageKey;

	public PlayerDataException(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
