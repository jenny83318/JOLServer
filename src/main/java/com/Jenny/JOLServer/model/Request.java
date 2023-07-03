package com.Jenny.JOLServer.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
	private String fun;
	private String account;
	private String token;
	private String type;
	private Map<String, Object> body;
}
