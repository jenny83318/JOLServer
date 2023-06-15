package com.Jenny.JOLServer.model;

import lombok.Data;

@Data
public class StripePay {
	private String name;
	private String currency;
	private String successUrl;
	private String cancelUrl;
	private long amount;
	private long quantity;

}
