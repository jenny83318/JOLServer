package com.Jenny.JOLServer.common;

public enum Fun {
	LOGIN("LogIn"),
	CUSTOMER("JOLCustomerInfo"),
	PRODUCT("JOLProductInfo"),
	CART("JOLCartInfo"),
	ORDER("JOLOrderInfo"),
	ORDERDETAIL("JOLOrderDetailInfo");

	private final String funName;

	Fun(String funName) {
		this.funName = funName;
	}

	public String getFunName() {
		return this.funName;
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
