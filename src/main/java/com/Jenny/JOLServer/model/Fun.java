package com.Jenny.JOLServer.model;

public enum Fun {
	LOGIN("LogIn"),
	CUSTOMER("JOLCustomerInfo"); 

	private final String funName;

	Fun(String funName) {
		this.funName = funName;
	}

	public String getFunName() {
		return funName;
	}
	public static Fun getFunValue(String value) {
        for (Fun f : Fun.values()) {
            if (f.getFunName().equals(value)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Invalid fun: " + value);
    }
}
