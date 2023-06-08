package com.Jenny.JOLServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.common.Fun;
import com.Jenny.JOLServer.fun.JOLCartInfo;
import com.Jenny.JOLServer.fun.JOLCustomerInfo;
import com.Jenny.JOLServer.fun.JOLOrderDetailInfo;
import com.Jenny.JOLServer.fun.JOLOrderInfo;
import com.Jenny.JOLServer.fun.JOLPayInfo;
import com.Jenny.JOLServer.fun.JOLProductInfo;
import com.Jenny.JOLServer.fun.LogIn;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;
@RequestMapping("/api")
@RestController
public class MainController {
	
	@Autowired()
	private LogIn logIn;
	
	@Autowired()
	private JOLCustomerInfo custService;	
	
	@Autowired()
	private JOLCartInfo cartService;
	
	@Autowired()
	private JOLProductInfo prodService;
	
	@Autowired()
	private JOLOrderInfo orderService;
	
	@Autowired()
	private JOLOrderDetailInfo detailService;
	
	@Autowired()
	private JOLPayInfo payService;
	
	@PostMapping("/json")
	public Object Dispatcher(@RequestBody Request req) throws Exception{
		if(req.getFun().isEmpty() ) {
			throw new CustomException("PARAM NOT FOUND: fun");
		}
		if(req.getAccount().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: accounts");
		}
		if(! "LogIn".equals(req.getFun()) && req.getType().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		Object out = new Object();
		Fun fun = Fun.getFunValue(req.getFun());
		switch (fun) {
		    case LOGIN:
		    	out = logIn.doProcess(req);
		        break;
		    case CUSTOMER:
		    	out = custService.doProcess(req);
		        break;
		    case CART:
		    	out = cartService.doProcess(req);
		        break;
		    case PRODUCT:
		    	out = prodService.doProcess(req);
		        break;
		    case ORDER:
		    	out = orderService.doProcess(req);
		        break;
		    case ORDERDETAIL:
		    	out = detailService.doProcess(req);
		    	break;
		    case PAY:
		    	out = payService.doProcess(req);
		    	break;
		}
		return out;
	}
}
