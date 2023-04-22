package com.Jenny.JOLServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.service.JOLCustomerInfo;
import com.Jenny.JOLServer.service.LogIn;
import com.Jenny.JOLServer.tool.CustomException;
@RequestMapping("/api")
@RestController
public class MainController {
	
	@Autowired()
	private JOLCustomerInfo custService;	
	
	@Autowired()
	private LogIn logIn;
	
	@PostMapping("/json")
	public Object Dispatcher(@RequestBody Request req) throws Exception{
		if(req.getFun().isEmpty() ) {
			throw new CustomException("PARAM NOT FOUND: fun");
		}
		Object out = new Object();
		if("JOLCustomerInfo".equals(req.getFun())) {
			out = custService.doProcess(req);
		}
		if("LogIn".equals(req.getFun())){
			out = logIn.doProcess(req);
		}
		return out;
	}

	
	
}
