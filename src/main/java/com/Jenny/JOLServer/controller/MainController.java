package com.Jenny.JOLServer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.model.Customer;
import com.Jenny.JOLServer.service.JOLCustomerInfo;

@RestController
public class MainController {

	@Autowired()
	private JOLCustomerInfo custService;
	
	@GetMapping("/index")
	public List<Customer> getHomeData() {
		return custService.getAllCustomerInfo();
	}

}



