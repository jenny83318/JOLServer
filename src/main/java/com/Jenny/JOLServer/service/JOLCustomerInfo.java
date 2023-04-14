package com.Jenny.JOLServer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.CustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Request JSON
{
    "account":"jenny83318",
    "body":{
    "type":0,
    "account": "abc1235",
    "password": "vjsklemmr",
    "name": "林曉涵",
    "phone": "0915526665",
    "email": "wewwwwwwww",
    "status": 1
}
 */
@Service
public class JOLCustomerInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLCustomerInfo.class);
	@Autowired
	private CustomerInfoDao custDao;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class REQUEST {
		private String account;
		private BODY body;
	
	}
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {	
		private Integer type;// 0:查全部、1:查單一、2:新增、3:編輯、4刪除
		private String password;
		private String name;
		private String phone;
		private String email;
		private String address;
		private Integer status;
	}

	@Data
	@Builder
	public static class OUT {
		private String result;
		private List<Customer> custList;
	}

	protected REQUEST check(REQUEST req) throws Exception  {
		if(req.account.isEmpty()) {
			throw new Exception("PARAM NOT FOUND: account");
		}
		return req;
	}
	public OUT doProcess(REQUEST req) throws Exception {
		check(req);
		log.info("REQ:{}", req);
		List<Customer> custList = new ArrayList<Customer>();
		if (req.getBody().getType() == 0) {
			custList = custDao.findAll();
		}
		return OUT.builder().custList(custList).result("success").build();

	}

}
