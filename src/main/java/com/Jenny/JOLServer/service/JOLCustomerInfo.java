package com.Jenny.JOLServer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.CustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;
import com.Jenny.JOLServer.model.Request;
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

	protected Request check(Request req) throws Exception {
		if (req.getAccount().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: account");
		}
		if (req.getFun().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: fun");
		}
		if (req.getBody().get("type") == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if ((int)req.getBody().get("type") != 0) {
			if (req.getAccount().isEmpty()) {
				throw new CustomException("PARAM NOT FOUND: account");
			}
		}
		if ((int)req.getBody().get("type") == 2 || (int)req.getBody().get("type") == 3) {
			if (req.getBody().get("email") == null) {
				throw new CustomException("PARAM NOT FOUND: email");
			}
			if (req.getBody().get("password") == null) {
				throw new CustomException("PARAM NOT FOUND: password");
			}
			if (req.getBody().get("name") == null) {
				throw new CustomException("PARAM NOT FOUND: name");
			}
			if (req.getBody().get("address") == null ) {
				throw new CustomException("PARAM NOT FOUND: address");
			}
			if (req.getBody().get("phone") == null) {
				throw new CustomException("PARAM NOT FOUND: phone");
			}
			if (req.getBody().get("status") == null) {
				throw new CustomException("PARAM NOT FOUND: status");
			}
		}
		return req;
	}
	
	public BODY parser(Map<String, Object> map) {
		 ModelMapper modelMapper = new ModelMapper();
	     BODY body = modelMapper.map(map, BODY.class);
		return body;
	}
	
	public OUT doProcess(Request req) throws Exception {
		check(req);
		BODY body = parser(req.getBody());
		List<Customer> custList = new ArrayList<Customer>();
		if (body.getType() == 0) {
			custList = custDao.findAll();
		}
		if (body.getType() == 1) {
			Customer c = custDao.findByAccount(req.getAccount());
			custList.add(c == null ? Customer.builder().build() : c );
		}
		if (body.getType() == 2 || body.getType() == 3) {
			Customer cust = custDao.findByAccount(req.getAccount());
			if (body.getType() == 2 && cust != null ) {
				throw new CustomException("此帳號已存在，無法新增");
			} else if (body.getType() == 3 && cust == null){
				throw new CustomException("此帳號不存在，無法更新");
			} else {
				Customer c = Customer.builder().account(req.getAccount()).address(body.address)
						.email(body.getEmail()).name(body.getName())
						.password(body.getPassword()).phone(body.getPhone())
						.status(body.getStatus()).payment(body.getPayment()).build();
				Customer updCustomer = custDao.save(c);
				if (updCustomer != null) {
					custList.add(updCustomer);
				} else {
					return OUT.builder().custList(custList).code(HttpStatus.BAD_REQUEST.value()).msg("新增失敗").build();
				}
			}
		}
		if (body.getType() == 4 ) {
			custDao.deleteByAccount(req.getAccount());
		}
		log.info("custList:{}", custList);
		return OUT.builder().custList(custList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

}
