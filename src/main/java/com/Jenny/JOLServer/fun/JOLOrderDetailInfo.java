package com.Jenny.JOLServer.fun;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.common.Type;
import com.Jenny.JOLServer.dao.OrderDetailDao;
import com.Jenny.JOLServer.model.OrderDetail;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class JOLOrderDetailInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLOrderDetailInfo.class);
	
	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private int orderDetailNo;
		private int orderNo;
		private int prodId;
		private int qty;
		private int price;
		private String status;
	}
	
	@Data
	@Builder
	public static class OUT {
		@Builder.Default
		private int code = 0;
		@Builder.Default
		private String msg = "";
	}
	
	
	protected Request check(Request req) throws Exception {
		if ("UPDATE".equals(req.getType())) {
			if (req.getBody().get("orderDetailNo") == null) {
				throw new CustomException("PARAM NOT FOUND: orderDetailNo");
			}
		}
		if ("ADD".equals(req.getType()) || "UPDATE".equals(req.getType())) {
			if (req.getBody().get("orderNo") == null) {
				throw new CustomException("PARAM NOT FOUND: orderNo");
			}
			if (req.getBody().get("prodId") == null) {
				throw new CustomException("PARAM NOT FOUND: prodId");
			}
			if (req.getBody().get("qty") == null) {
				throw new CustomException("PARAM NOT FOUND: qty");
			}
			if (req.getBody().get("price") == null) {
				throw new CustomException("PARAM NOT FOUND: price");
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
		log.info("body:{}",body.toString());
		Type type = Type.getType(req.getType());
		switch (type) {
		case ADD:
		case UPDATE:
			LocalDateTime currentTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String now = currentTime.format(formatter);
			OrderDetail o = orderDetailDao.save(OrderDetail.builder().account(req.getAccount())
						.orderDetailNo(body.getOrderDetailNo()).orderNo(body.getOrderNo())
						.price(body.getPrice()).prodId(body.getProdId()).qty(body.getQty())
						.status(body.getStatus()).updateDt(now).build());	
			if(o == null) {
				return OUT.builder().code(999).msg("execute FAIL").build();
			}
			break;
		default:
			break;
		}
		return OUT.builder().code(HttpStatus.OK.value()).msg("execute success.").build();
	}

}
