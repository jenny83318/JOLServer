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
import com.Jenny.JOLServer.model.Product;
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
		private String status;
		private String deliveryWay;
		private String deliveryNo;
		private String orderName;
		private String orderPhone;
		private String orderCity;
		private String orderDistrict;
		private String orderAddress;
		private String sendName;
		private String sendPhone;
		private String sendCity;
		private String sendDistrict;
		private String sendAddress;
		private String vehicle;
		private String vehicleType;
		private String payBy;	
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
		private String sendDistrict = "";
		@Builder.Default
		private String sendAddress = "";
		@Builder.Default
		private String vehicle = "";
		@Builder.Default
		private String vehicleType ="";
		@Builder.Default
		private String payBy="";	
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
			if (req.getBody().get("email") == null) {
				throw new CustomException("PARAM NOT FOUND: email");
			}
			if (req.getBody().get("status") == null) {
				throw new CustomException("PARAM NOT FOUND: status");
			}
			if (req.getBody().get("deliveryWay") == null) {
				throw new CustomException("PARAM NOT FOUND: deliveryWay");
			}
			if (req.getBody().get("deliveryNo") == null) {
				throw new CustomException("PARAM NOT FOUND: deliveryNo");
			}
			if (req.getBody().get("orderName") == null) {
				throw new CustomException("PARAM NOT FOUND: orderName");
			}
			if (req.getBody().get("orderPhone") == null) {
				throw new CustomException("PARAM NOT FOUND: orderPhone");
			}
			if (req.getBody().get("orderCity") == null) {
				throw new CustomException("PARAM NOT FOUND: orderCity");
			}
			if (req.getBody().get("orderDistrict") == null) {
				throw new CustomException("PARAM NOT FOUND: orderDistrict");
			}
			if (req.getBody().get("orderAddress") == null) {
				throw new CustomException("PARAM NOT FOUND: orderAddress");
			}
			if (req.getBody().get("sendName") == null) {
				throw new CustomException("PARAM NOT FOUND: sendName");
			}
			if (req.getBody().get("sendPhone") == null) {
				throw new CustomException("PARAM NOT FOUND: sendPhone");
			}
			if (req.getBody().get("sendCity") == null) {
				throw new CustomException("PARAM NOT FOUND: sendCity");
			}
			if (req.getBody().get("sendDistrict") == null) {
				throw new CustomException("PARAM NOT FOUND: sendDistrict");
			}
			if (req.getBody().get("sendAddress") == null) {
				throw new CustomException("PARAM NOT FOUND: sendAddress");
			}
			if (req.getBody().get("vehicleType") == null) {
				throw new CustomException("PARAM NOT FOUND: vehicleType");
			}
			if (req.getBody().get("payBy") == null) {
				throw new CustomException("PARAM NOT FOUND: payBy");
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
			Order o = orderDao.save(bodyToDB(body,req));
			dataList.add(dbToBean(o, new ArrayList<OrderDetail>()));
			break;
		default:
			break;
		}
		log.info("dataList:{}", dataList);
		return OUT.builder().code(HttpStatus.OK.value()).msg("execute success.").orderList(dataList).build();
	}
	
	public List<ORDER> getOrderDataByAccount(String account) {
		List<Order> orderList = orderDao.findByAccountOrderByOrderTimeDesc(account);
		List<ORDER> dataList = new ArrayList<ORDER>();
		List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
		if (orderList.size() > 0) {
			for (Order order : orderList) {
				orderDetail = orderDetailDao.findByOrderNo(order.getOrderNo());
				for(OrderDetail o : orderDetail) {
					Product p = productDao.findByProdId(o.getProdId());
					o.setImgUrl(p.getImgUrl());
					o.setProdName(p.getName());
				}
				dataList.add(dbToBean(order, orderDetail));
			}
		}
		return dataList;
	}
	
	
	public ORDER dbToBean(Order order, List<OrderDetail> detailList) {
		return ORDER.builder().orderNo(order.getOrderNo()).account(order.getAccount())
				.email(order.getEmail()).totalAmt(order.getTotalAmt())
				.orderTime(order.getOrderTime()).status(order.getStatus())
				.deliveryWay(order.getDeliveryWay()).deliveryNo(order.getDeliveryNo())
				.orderName(order.getOrderName()).orderCity(order.getOrderCity())
				.orderPhone(order.getOrderPhone()).orderDistrict(order.getOrderDistrict())
				.orderAddress(order.getOrderAddress()).sendName(order.getSendName())
				.sendCity(order.getSendCity()).sendDistrict(order.getSendDistrict())
				.sendAddress(order.getSendAddress()).updateDt(order.getUpdateDt())
				.vehicle(order.getVehicle()).vehicleType(order.getVehicleType())
				.payBy(order.getPayBy()).orderDetail(detailList).build();
	}
	
	public Order bodyToDB(BODY body, Request req) {
		LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = currentTime.format(formatter);
		return Order.builder()
				.orderNo(body.getOrderNo()).account(req.getAccount())
				.email(body.getEmail()).totalAmt(body.getTotalAmt())
				.orderTime(now).status(body.getStatus()).deliveryWay(body.getDeliveryWay())
				.deliveryNo(body.getDeliveryNo()).orderName(body.getOrderName())
				.orderCity(body.getOrderCity()).orderPhone(body.getOrderPhone())
				.orderDistrict(body.getOrderDistrict()).orderAddress(body.getOrderAddress())
				.sendName(body.getSendName()).sendCity(body.getSendCity())
				.sendDistrict(body.getSendDistrict()).sendAddress(body.getSendAddress())
				.updateDt(now).vehicle(body.getVehicle()).sendPhone(body.getSendPhone())
				.vehicleType(body.getVehicleType()).payBy(body.getPayBy()).build();
	}
	
}
