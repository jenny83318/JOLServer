package com.Jenny.JOLServer.fun;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.http.LinePay;
import com.Jenny.JOLServer.model.Request;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class JOLPayInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLCartInfo.class);
	
	@Autowired
	private LinePay linepay;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private String prime;
		private String partner_key;
		private String merchant_id;
		private String details;
		private String amount;
		private boolean remember;
		private CardHolder cardholder;
	}
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CardHolder {
		private String phone_number;
		private String name;
		private String email;
		private String zip_code;
		private String address;
		private String national_id;
	}
	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
	}
	
	public BODY parser(Map<String, Object> map) {
		ModelMapper modelMapper = new ModelMapper();
		BODY body = modelMapper.map(map, BODY.class);
		return body;
	}
   
	public OUT doProcess(Request req) throws Exception {
		BODY body = parser(req.getBody());
		log.info("body:{}",body);
		ObjectMapper objMapper = new ObjectMapper();
		String json = objMapper.writeValueAsString(body);
		String reponse = linepay.call(json);
		log.info("reponse:{}",reponse);
		return OUT.builder().code(HttpStatus.OK.value()).msg(reponse).build();
		
	}
}
