package com.Jenny.JOLServer.fun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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

@Service
public class LogIn {
	private static final Logger log = LoggerFactory.getLogger(LogIn.class);
	@Autowired
	private CustomerInfoDao custDao;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private String password;
	}

	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private String token;
		private String email;
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
			if (!"CHECK".equals(req.getType())) {
				String token = UUID.randomUUID().toString();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.HOUR_OF_DAY, 24);
				String nextDayStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
				c.setTokenExpired(nextDayStr);
				c.setToken(token);
				Customer updCust = custDao.save(c);
				out = OUT.builder().code(HttpStatus.OK.value()).msg("登入成功").token(updCust.getToken()).tokenExpired(updCust.getTokenExpired()).email(updCust.getEmail()).build();
			}
			else if ("CLEAN".equals(req.getType())) {
				c.setTokenExpired(null);
				c.setToken(null);
				Customer cleanCust = custDao.save(c);
				log.info("CLEAN:{}", cleanCust);
				out = OUT.builder().code(HttpStatus.OK.value()).msg("登入成功").token(cleanCust.getToken())
						.tokenExpired(cleanCust.getTokenExpired()).email(cleanCust.getEmail()).build();
			} else {
				out = OUT.builder().code(HttpStatus.OK.value()).msg("查詢成功").token(c.getToken()).tokenExpired(c.getTokenExpired()).email(c.getEmail()).build();
			}
		} else {
			out = OUT.builder().code(555).msg("帳號或密碼錯誤，請重新輸入。").token(null).tokenExpired(null).email(null).build();
		}
		return out;
	}

	public OUT checkToken(String account, String token) {
		OUT out = OUT.builder().build();
		try {
			Customer c = custDao.findByAccountAndToken(account, token);
			if (c != null) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date tokenExpried;
				tokenExpried = df.parse(c.getTokenExpired());
				if (tokenExpried.before(new Date())) {
					out = OUT.builder().code(666).msg("token過期").token(c.getToken()).tokenExpired(c.getTokenExpired())
							.email(c.getEmail()).build();
				} else {
					out = OUT.builder().code(HttpStatus.OK.value()).msg("SUCCESS").token(c.getToken())
							.tokenExpired(c.getTokenExpired()).email(c.getEmail()).build();
				}
			} else {
				out = OUT.builder().code(666).msg("查無此帳號Token").build();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("PARSER ERROR: {}", e.getMessage());
		}
		return out;

	}
}
