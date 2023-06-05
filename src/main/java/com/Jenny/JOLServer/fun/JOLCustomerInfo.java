package com.Jenny.JOLServer.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.common.Type;
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
// 0:查全部、1:查單一、2:新增、3:編輯、4刪除
{
    "account":"jenny83318",
    "type":0,
    "fun":"JOLCustomerInfo",
    "body":{
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
		private String password;
		private String name;
		private String phone;
		private String email;
		private String city;
		private String district;
		private String address;
		private Integer status;
	}

	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private List<Customer> custList;
	}

	protected Request check(Request req) throws Exception {
		if ("ADD".equals(req.getType()) || "UPDATE".equals(req.getType())) {
			if (req.getBody().get("email") == null) {
				throw new CustomException("PARAM NOT FOUND: email");
			}
			if (req.getBody().get("password") == null) {
				throw new CustomException("PARAM NOT FOUND: password");
			}
			if (req.getBody().get("name") == null) {
				throw new CustomException("PARAM NOT FOUND: name");
			}
			if (req.getBody().get("address") == null) {
				throw new CustomException("PARAM NOT FOUND: address");
			}
			if (req.getBody().get("phone") == null) {
				throw new CustomException("PARAM NOT FOUND: phone");
			}
			if (req.getBody().get("status") == null) {
				throw new CustomException("PARAM NOT FOUND: status");
			}
			if (req.getBody().get("city") == null) {
				throw new CustomException("PARAM NOT FOUND: city");
			}
			if (req.getBody().get("district") == null) {
				throw new CustomException("PARAM NOT FOUND: district");
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
		Type type = Type.getType(req.getType());
		switch (type) {
		case ALL:
			custList = custDao.findAll();
			break;
		case SELECT:
			Customer c = custDao.findByAccount(req.getAccount());
			custList.add(c == null ? Customer.builder().build() : c);
			break;
		case ADD:
		case UPDATE:
			Customer cust = custDao.findByAccount(req.getAccount());
			if ("ADD".equals(type.getTypeName()) && cust != null) {
				throw new CustomException("此帳號已存在，無法新增");
			} else if ("UPDATE".equals(type.getTypeName()) && cust == null) {
				throw new CustomException("此帳號不存在，無法更新");
			} else {
				Customer newCust = Customer.builder()
						.account(req.getAccount())
						.address(body.address)
						.email(body.getEmail())
						.name(body.getName())
						.password(body.getPassword())
						.phone(body.getPhone())
						.city(body.getCity())
						.district(body.getDistrict())
						.status(body.getStatus()).build();
				Customer updCustomer = custDao.save(newCust);
				if (updCustomer != null) {
					custList.add(updCustomer);
				} else {
					return OUT.builder().custList(custList).code(HttpStatus.BAD_REQUEST.value()).msg("新增失敗").build();
				}
			}
			break;
		case DELETE:
			custDao.deleteByAccount(req.getAccount());
			break;
		default:
			break;
		}
		log.info("custList:{}", custList);
		return OUT.builder().custList(custList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

}
