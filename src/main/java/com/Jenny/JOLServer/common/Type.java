package com.Jenny.JOLServer.common;

public enum Type {
	ALL("ALL"),
	SELECT("SELECT"),
	ADD("ADD"),
	DELETE("DELETE"),
	UPDATE("UPDATE");
	
	private final String type;
	
	Type(String type) {
		this.type = type;
	}
	
	public String getTypeName() {
		return this.type;
	}
	
	public static Type getType(String typeName) {
		for(Type type: Type.values()) {
			if(type.getTypeName().equals(typeName)) {
				return type;
			}
		}
		return null;
	}

}
