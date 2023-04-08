package com.Jenny.JOLServer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.JOLCustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;

@Service
public class JOLCustomerInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLCustomerInfo.class);
	@Autowired 
	private JOLCustomerInfoDao custDao;
	
	public List <Customer> getAllCustomerInfo() {
		List <Customer> custList = new ArrayList<>();
		custList = custDao.findAll();
		for(Customer c : custList) {
			log.info(c.toString());
			
		}
		
		return custList;
		
	}
	
	

}
