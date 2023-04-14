package com.Jenny.JOLServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.service.JOLCustomerInfo;
import com.Jenny.JOLServer.service.JOLCustomerInfo.REQUEST;
@RequestMapping("/api/json")
@RestController
public class MainController {
	
	@Autowired()
	private JOLCustomerInfo custService;	
	
	@PostMapping("/customer")
	public JOLCustomerInfo.OUT CustomerInfo(@RequestBody REQUEST req) throws Exception{
		return custService.doProcess(req);
	}

	
}
