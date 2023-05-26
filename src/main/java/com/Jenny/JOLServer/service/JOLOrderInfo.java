package com.Jenny.JOLServer.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.Jenny.JOLServer.dao.OrderDetailDao;
import com.Jenny.JOLServer.dao.OrderInfoDao;
import com.Jenny.JOLServer.dao.ProductInfoDao;
import com.Jenny.JOLServer.model.Order;
import com.Jenny.JOLServer.model.OrderDetail;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * {
    "fun":"JOLOrderInfo",
    "account":"peter1234",
    "body":{
        "type":"ADD",
        "totalAmt":3630,
        "shipNo":"263622",
        "isOrderDetail":0,
        "status":"prepare"
        
    }
    
 */
@Service
public class JOLOrderInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLOrderInfo.class);

	@Autowired
	private OrderInfoDao orderDao;

	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private ProductInfoDao productDao;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private String type;
		private int orderNo;
		private int totalAmt;
		private String shipNo;
		private String status;
	}

	@Data
	@Builder
	public static class OUT {
		@Builder.Default
		private int code = 0;
		@Builder.Default
		private String msg = "";
		@Builder.Default
		private List<ORDER> orderList = new ArrayList<ORDER>();
	}
	
	@Data
	@Builder
	public static class ORDER {
		@Builder.Default
		private int orderNo = 0;
		@Builder.Default
		private int totalAmt = 0;
		@Builder.Default
		private String orderTime = "";
		@Builder.Default
		private String shipNo = "";
		@Builder.Default
		private String status = "";
		@Builder.Default
		private String updateDt = "";
		@Builder.Default
		private List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
	}

	protected Request check(Request req) throws Exception {
		if (req.getBody().get("type") == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if ("UPDATE".equals(req.getBody().get("type"))) {
			if (req.getBody().get("orderNo") == null) {
				throw new CustomException("PARAM NOT FOUND: orderNo");
			}
		}
		if ("ADD".equals(req.getBody().get("type")) || "UPDATE".equals(req.getBody().get("type"))) {
			if (req.getBody().get("totalAmt") == null) {
				throw new CustomException("PARAM NOT FOUND: totalAmt");
			}
			if (req.getBody().get("shipNo") == null) {
				throw new CustomException("PARAM NOT FOUND: shipNo");
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
		Type type = Type.getType(body.getType());
		List<ORDER> dataList = new ArrayList<ORDER>();
		switch (type) {
		case SELECT:
			dataList = getOrderDataByAccount(req.getAccount());
			break;
		case ADD:
		case UPDATE:
			LocalDateTime currentTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String now = currentTime.format(formatter);
			Order o = orderDao.save(Order.builder().account(req.getAccount())
					.orderTime(now).shipNo(body.getShipNo())
					.status(body.getStatus()).totalAmt(body.getTotalAmt()).updateDt(now).build());
			dataList.add(ORDER.builder().orderNo(o.getOrderNo()).orderTime(o.getOrderTime())
					.totalAmt(o.getTotalAmt()).shipNo(o.getShipNo()).status(o.getStatus())
					.updateDt(o.getUpdateDt()).build());
			break;
		default:
			break;
		}
		log.info("dataList:{}", dataList);
		return OUT.builder().code(HttpStatus.OK.value()).msg("execute success.").orderList(dataList).build();
	}
	
	public List<ORDER> getOrderDataByAccount(String account) {
		List<Order> orderList = orderDao.findByAccount(account);
		List<ORDER> dataList = new ArrayList<ORDER>();
		List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
		if (orderList.size() > 0) {
			for (Order order : orderList) {
				orderDetail = orderDetailDao.findByOrderNo(order.getOrderNo());
				for(OrderDetail o : orderDetail) {
					o.setImgUrl(productDao.findByProdId(o.getProdId()).getImgUrl());
				}
				dataList.add(ORDER.builder()
				.orderNo(order.getOrderNo()).totalAmt(order
			    .getTotalAmt()).orderTime(order.getOrderTime())
				.shipNo(order.getShipNo()).status(order.getStatus())
				.updateDt(order.getUpdateDt()).orderDetail(orderDetail).build());
			}
		}
		return dataList;
	}
	
}
