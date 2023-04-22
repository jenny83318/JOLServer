package com.Jenny.JOLServer.service;

import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.CustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;
import com.Jenny.JOLServer.model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class LogIn {
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
		private String password;
		private String token;
	}
	
	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private String token;
		private String tokenExpired;
	}
	
	public BODY parser(Map<String, Object> map) {
		 ModelMapper modelMapper = new ModelMapper();
	     BODY body = modelMapper.map(map, BODY.class);
		return body;
	}

	public OUT doProcess(Request req) throws Exception {
		BODY body = parser(req.getBody());
		Customer c = custDao.findByAccountAndPassword(req.getAccount(),body.getPassword());
		OUT out = OUT.builder().build();
		if(c != null) {
			if(c.getToken() == null) {
				String token = UUID.randomUUID().toString();
				c.setToken(token);
				Customer updCust = custDao.save(c);
				out = OUT.builder().code(HttpStatus.OK.value()).msg("Success").token(updCust.getToken()).tokenExpired(updCust.getTokenExpired()).build();
			}else {
				if(c.getToken().equals(body.getToken())) {
					out= OUT.builder().code(HttpStatus.OK.value()).msg("Success").token(c.getToken()).tokenExpired(c.getTokenExpired()).build();
				}else {
					out = OUT.builder().code(999).msg("Fail").token(c.getToken()).tokenExpired(c.getTokenExpired()).build();
				}
			}
		}else {
			out = OUT.builder().code(555).msg("Not found").token(null).tokenExpired(null).build();
		}
		return out;
	}
}
