package com.Jenny.JOLServer.service;

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
import com.Jenny.JOLServer.dao.CartInfoDao;
import com.Jenny.JOLServer.model.Cart;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class JOLCartInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLCartInfo.class);
	@Autowired
	private CartInfoDao cartDao;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private int cartId;
		private int prodId;
		private int qty;
		private String type;
		private String size;
		private String updateDt;
		private boolean isCart;
	}

	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private List<Cart> cartList;
	}

	protected Request check(Request req) throws Exception {
		if (req.getAccount().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: account");
		}
		if (req.getBody().get("type") == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if (req.getBody().get("isCart") == null) {
			throw new CustomException("PARAM NOT FOUND: isCart");
		}
		if (!"SELECT".equals(req.getBody().get("type"))) {
			if (req.getBody().get("cartId") == null) {
				throw new CustomException("PARAM NOT FOUND: cartId");
			}
		}
		if ("ADD".equals(req.getBody().get("type")) || "UPDATE".equals(req.getBody().get("type"))) {
			if (req.getBody().get("prodId") == null) {
				throw new CustomException("PARAM NOT FOUND: prodId");
			}
			if (req.getBody().get("qty") == null) {
				throw new CustomException("PARAM NOT FOUND: qty");
			}
			if (req.getBody().get("size") == null) {
				throw new CustomException("PARAM NOT FOUND: size");
			}
			if (req.getBody().get("updateDt") == null) {
				throw new CustomException("PARAM NOT FOUND: updateDt");
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
		List<Cart> cartList = new ArrayList<Cart>();
		Type type = Type.getType(body.getType());
		switch (type) {
		case SELECT:
			cartList = this.cartDao.findByAccountAndIsCart(req.getAccount(), body.isCart);
			break;
		case ADD:
		case UPDATE:
			Cart newCart = this.cartDao.save(
					Cart.builder()
					.account(req.getAccount())
					.cartId(body.getCartId())
					.isCart(body.isCart)
					.prodId(body.getProdId())
					.qty(body.getQty())
					.size(body.getSize())
					.updateDt(body.getUpdateDt()).build());
			if (newCart != null) {
				cartList = this.cartDao.findByAccountAndIsCart(req.getAccount(), body.isCart);
			} else {
				return OUT.builder().code(999).msg("新增或更新失敗").build();
			}
			break;
		case DELETE:
			cartDao.deleteById(body.getCartId());
			break;
		default:
			break;
		}
		log.info("cartList:{}", cartList);
		return OUT.builder().cartList(cartList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}
}
