package com.Jenny.JOLServer.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
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

@Service
public class LogIn {
	@Autowired
	private CustomerInfoDao custDao;

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
	
	protected Request check(Request req) throws Exception {
		if (req.getBody().get("password") == null) {
			throw new CustomException("PARAM NOT FOUND: password");
		}
		if (req.getBody().get("token") == null) {
			throw new CustomException("PARAM NOT FOUND: token");
		}
		
		return req;
	}

	public OUT doProcess(Request req) throws Exception {
		BODY body = parser(req.getBody());
		Customer c = custDao.findByAccountAndPassword(req.getAccount(), body.getPassword());
		OUT out = OUT.builder().build();
		if (c != null) {
			if (c.getToken() == null) {
				String token = UUID.randomUUID().toString();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.HOUR_OF_DAY, 24);
				String nextDayStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
				c.setTokenExpired(nextDayStr);
				c.setToken(token);
				Customer updCust = custDao.save(c);
				out = OUT.builder().code(HttpStatus.OK.value()).msg("Success").token(updCust.getToken()).tokenExpired(updCust.getTokenExpired()).build();
			} else {
				if (c.getToken().equals(body.getToken())) {
			        LocalDateTime expired = LocalDateTime.parse(c.getTokenExpired(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					int code = LocalDateTime.now().isAfter(expired) ? 777 : HttpStatus.OK.value();
					String msg = code == 777 ? "token is expired" : "Success";
					out = OUT.builder().code(code).msg(msg).token(c.getToken()).tokenExpired(c.getTokenExpired()).build();
				} else {
					out = OUT.builder().code(999).msg("Fail").token(c.getToken()).tokenExpired(c.getTokenExpired()).build();
				}
			}
		} else {
			out = OUT.builder().code(555).msg("Account or Password Error").token(null).tokenExpired(null).build();
		}
		return out;
	}
}
