package com.Jenny.JOLServer.fun;

import java.lang.reflect.Field;
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
import com.Jenny.JOLServer.dao.ProductInfoDao;
import com.Jenny.JOLServer.model.OrderDetail;
import com.Jenny.JOLServer.model.Product;
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

	@Autowired
	private ProductInfoDao productDao;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DTBODY {
		private int orderDetailNo;
		private int orderNo;
		private String size;
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
		@Builder.Default
		private List<OrderDetail> detailList = new ArrayList<OrderDetail>();
	}

	protected Request check(Request req) throws Exception {
		Field[] fields = DTBODY.builder().build().getClass().getDeclaredFields();
		for (Field field : fields) {
			String key = field.getName();
			Object value = req.getBody().get(key);
			if ("orderNo".equals(key) && value == null) {
				throw new CustomException("PARAM NOT FOUND: " + key);
			}
			if ("UPDATE".equals(req.getType()) && "orderDetailNo".equals(key)) {
				if (value == null) {
					throw new CustomException("PARAM NOT FOUND: " + key);
				}
			}
			if (("ADD".equals(req.getType()) && !"orderDetailNo".equals(key)) || "UPDATE".equals(req.getType())) {
				if (value == null) {
					throw new CustomException("PARAM NOT FOUND: " + key);
				}
			}
		}
		return req;
	}

	public DTBODY parser(Map<String, Object> map) {
		ModelMapper modelMapper = new ModelMapper();
		DTBODY body = modelMapper.map(map, DTBODY.class);
		return body;
	}

	public OUT doProcess(Request req) throws Exception {
		check(req);
		DTBODY body = parser(req.getBody());
		log.info("body:{}", body.toString());
		List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
		Type type = Type.getType(req.getType());
		switch (type) {
		case SELECT:
			orderDetail = orderDetailDao.findByOrderNoAndAccountOrderByProdId(body.getOrderNo(), req.getAccount());
			for (OrderDetail o : orderDetail) {
				log.info("detail:{}", o);
				Product p = productDao.findByProdId(o.getProdId());
				log.info("Product:{}", p);
				o.setImgUrl(p.getImgUrl());
				o.setProdName(p.getName());
			}
			break;
		case ADD:
		case UPDATE:
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = currentTime.format(formatter);
			OrderDetail o = orderDetailDao.save(OrderDetail.builder().account(req.getAccount())
					.orderDetailNo(body.getOrderDetailNo()).orderNo(body.getOrderNo()).price(body.getPrice())
					.prodId(body.getProdId()).qty(body.getQty()).size(body.getSize()).status(body.getStatus()).updateDt(now).build());
			if (o == null) {
				return OUT.builder().code(999).msg("execute FAIL").build();
			}
			break;
		default:
			break;
		}
		return OUT.builder().code(HttpStatus.OK.value()).msg("execute success.").detailList(orderDetail).build();
	}

}
