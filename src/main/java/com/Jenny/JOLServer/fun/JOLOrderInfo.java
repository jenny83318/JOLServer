package com.Jenny.JOLServer.fun;

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
		private int orderNo;
		private String email;
		private int totalAmt;
		private String orderTime;
		private String status;
		private String deliveryWay;
		private String deliveryNo;
		private String orderName;
		private String orderPhone;
		private String orderCity;
		private String orderDistrict;
		private String orderAddress;
		private String sendName;
		private String sendCity;
		private String sendAddress;
		private String vehicle;
		private String updateDt;
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
		private String account = "";
		@Builder.Default
		private String email = "";
		@Builder.Default
		private int totalAmt = 0;
		@Builder.Default
		private String orderTime = "";
		@Builder.Default
		private String status = "";
		@Builder.Default
		private String deliveryWay = "";
		@Builder.Default
		private String deliveryNo = "";
		@Builder.Default
		private String orderName = "";
		@Builder.Default
		private String orderPhone = "";
		@Builder.Default
		private String orderCity = "";
		@Builder.Default
		private String orderDistrict = "";
		@Builder.Default
		private String orderAddress = "";
		@Builder.Default
		private String sendName = "";
		@Builder.Default
		private String sendCity = "";
		@Builder.Default
		private String sendAddress = "";
		@Builder.Default
		private String vehicle = "";
		@Builder.Default
		private String updateDt = "";
		@Builder.Default
		private List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
	}

	protected Request check(Request req) throws Exception {
		if ("UPDATE".equals(req.getType())) {
			if (req.getBody().get("orderNo") == null) {
				throw new CustomException("PARAM NOT FOUND: orderNo");
			}
		}
		if ("ADD".equals(req.getType()) || "UPDATE".equals(req.getType())) {
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
		Type type = Type.getType(req.getType());
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
			Order o = orderDao.save(Order.builder()
					.orderNo(body.getOrderNo()).account(req.getAccount())
					.email(body.getEmail()).totalAmt(body.getTotalAmt())
					.orderTime(now).status(body.getStatus())
					.deliveryWay(body.getDeliveryWay()).deliveryNo(body.getDeliveryNo())
					.orderName(body.getOrderName()).orderCity(body.getOrderCity())
					.orderPhone(body.getOrderPhone()).orderDistrict(body.getOrderDistrict())
					.orderAddress(body.getOrderAddress()).sendName(body.getSendName())
					.sendCity(body.getSendCity()).sendAddress(body.getSendAddress())
					.updateDt(now).vehicle(body.getVehicle()).build());
			dataList.add(ORDER.builder().orderNo(o.getOrderNo()).account(o.getAccount())
					.email(o.getEmail()).totalAmt(o.getTotalAmt())
					.orderTime(o.getOrderTime()).status(o.getStatus())
					.deliveryWay(o.getDeliveryWay()).deliveryNo(o.getDeliveryNo())
					.orderName(o.getOrderName()).orderCity(o.getOrderCity())
					.orderPhone(o.getOrderPhone()).orderDistrict(o.getOrderDistrict())
					.orderAddress(o.getOrderAddress()).sendName(o.getSendName())
					.sendCity(o.getSendCity()).sendAddress(o.getSendAddress())
					.updateDt(o.getUpdateDt()).vehicle(o.getVehicle()).build());
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
				dataList.add(ORDER.builder().orderNo(order.getOrderNo()).account(order.getAccount())
						.email(order.getEmail()).totalAmt(order.getTotalAmt())
						.orderTime(order.getOrderTime()).status(order.getStatus())
						.deliveryWay(order.getDeliveryWay()).deliveryNo(order.getDeliveryNo())
						.orderName(order.getOrderName()).orderCity(order.getOrderCity())
						.orderPhone(order.getOrderPhone()).orderDistrict(order.getOrderDistrict())
						.orderAddress(order.getOrderAddress()).sendName(order.getSendName())
						.sendCity(order.getSendCity()).sendAddress(order.getSendAddress())
						.updateDt(order.getUpdateDt()).vehicle(order.getVehicle()).build());
			}
		}
		return dataList;
	}
	
}
