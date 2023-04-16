package com.Jenny.JOLServer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.CustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;
import com.Jenny.JOLServer.tool.CustomException;

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
		private String payment;
	}

	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private List<Customer> custList;
	}

	protected REQUEST check(REQUEST req) throws Exception {
		if (req.getBody().getType() == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if (req.getBody().getType() != 0) {
			if (req.account.isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: account");
			}
		}
		if (req.getBody().getType() == 2 || req.getBody().getType() == 3) {
			if (req.getBody().getEmail().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: email");
			}
			if (req.getBody().getPassword().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: password");
			}
			if (req.getBody().getName().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: name");
			}
			if (req.getBody().getAddress().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: address");
			}
			if (req.getBody().getPhone().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: phone");
			}
			if (req.getBody().getStatus() == null) {
				throw new CustomException("PARAM NOT FOUND: status");
			}
		}
		return req;
	}

	public OUT doProcess(REQUEST req) throws Exception {
		check(req);
		List<Customer> custList = new ArrayList<Customer>();
		if (req.getBody().getType() == 0) {
			custList = custDao.findAll();
		}
		if (req.getBody().getType() == 1) {
			custList.add(custDao.findByAccount(req.getAccount()));
		}
		if (req.getBody().getType() == 2 || req.getBody().getType() == 3) {
			Customer cust = custDao.findByAccount(req.getAccount());
			if (req.getBody().getType() == 2 && cust != null ) {
				throw new CustomException("此帳號已存在，無法新增");
			} else if (req.getBody().getType() == 3 && cust == null){
				throw new CustomException("此帳號不存在，無法更新");
			} else {
				Customer c = Customer.builder().account(req.getAccount()).address(req.getBody().address)
						.email(req.getBody().getEmail()).name(req.getBody().getName())
						.password(req.getBody().getPassword()).phone(req.getBody().getPhone())
						.status(req.getBody().getStatus()).payment(req.getBody().getPayment()).build();
				Customer updCustomer = custDao.save(c);
				if (updCustomer != null) {
					custList.add(updCustomer);
				} else {
					return OUT.builder().custList(custList).code(HttpStatus.BAD_REQUEST.value()).msg("新增失敗").build();
				}
			}
		}
		if (req.getBody().getType() == 4 ) {
			custDao.deleteByAccount(req.getAccount());
		}
		log.info("custList:{}", custList);
		return OUT.builder().custList(custList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

}
