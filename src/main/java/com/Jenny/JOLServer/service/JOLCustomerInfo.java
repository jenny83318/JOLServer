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
{
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
	public static class IN {
		private Integer type;// 0:查全部、1:查單一、2:新增、3:編輯、4刪除
		private String account;
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
		private List<CUSTOMER> custList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CUSTOMER {
		private String account;
		private String password;
		private String name;
		private String phone;
		private String email;
		private String address;
		private Integer status;
	}

	public OUT doProcess(IN in) {
		List<CUSTOMER> custList = new ArrayList<CUSTOMER>();
		if (in.getType() == 0) {
			List<Customer> custData = custDao.findAll();
			for (Customer c : custData) {
				custList.add(CUSTOMER.builder().account(c.getAccount()).password(c.getPassword()).name(c.getName())
						.phone(c.getPhone()).email(c.getEmail()).address(c.getAddress()).status(c.getStatus()).build());
			}
		}
		return OUT.builder().custList(custList).result("success").build();

	}

}
