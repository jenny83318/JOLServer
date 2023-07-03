package com.Jenny.JOLServer.fun;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.Jenny.JOLServer.dao.CartInfoDao;
import com.Jenny.JOLServer.dao.ProductInfoDao;
import com.Jenny.JOLServer.model.Cart;
import com.Jenny.JOLServer.model.Product;
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

	@Autowired
	private ProductInfoDao productDao;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private int cartId;
		private int prodId;
		private int qty;
		private String size;
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
		Field[] fields = BODY.builder().build().getClass().getDeclaredFields();
		for (Field field : fields) {
			String key = field.getName();
			Object value = req.getBody().get(key);
			if (!"ALL".equals(req.getType()) && "isCart".equals(key) && value == null) {
				throw new CustomException("PARAM NOT FOUND: " + key);
			}
			if (("UPDATE".equals(req.getType()) || "DELETE".equals(req.getType())) && "cartId".equals(key)
					&& value == null) {
				throw new CustomException("PARAM NOT FOUND: " + key);
			}
			if ("ADD".equals(req.getType()) || "UPDATE".equals(req.getType())) {
				if ((!"cartId".equals(key) && !"isCart".equals(key)) && value == null) {
					throw new CustomException("PARAM NOT FOUND: " + key);
				}
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
		Type type = Type.getType(req.getType());
		switch (type) {
		case ALL:
			cartList = getProductByCart(cartDao.findByAccountOrderByUpdateDtDesc(req.getAccount()));
			break;
		case SELECT:
			cartList = getProductByCart(
					cartDao.findByAccountAndIsCartOrderByUpdateDtDesc(req.getAccount(), body.isCart));
			break;
		case OTHER:
			cartList = getProductByCart(
					cartDao.findByAccountAndIsCartAndProdId(req.getAccount(), body.isCart(), body.getProdId()));
			break;
		case ADD:
			return addCart(body, req);
		case UPDATE:
			return updateCart(body, req);
		case DELETE:
			cartDao.deleteById(body.getCartId());
			break;
		default:
			break;
		}
		return OUT.builder().cartList(cartList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

	public List<Cart> getProductByCart(List<Cart> cartList) {
		for (Cart c : cartList) {
			Product p = productDao.findByProdId(c.getProdId());
			c.setProdName(p.getName());
			c.setPrice(p.getPrice());
			c.setSizeInfo(p.getSizeInfo());
			c.setImgUrl(p.getImgUrl());
		}
		return cartList;
	}

	public OUT addCart(BODY body, Request req) {
		List<Cart> cartList = new ArrayList<Cart>();
		List<Cart> cartProdList = new ArrayList<Cart>();
		if (body.isCart()) {
			cartProdList = cartDao.findByAccountAndIsCartAndProdIdAndSize(req.getAccount(), body.isCart,
					body.getProdId(), body.getSize());
		} else {
			cartProdList = cartDao.findByAccountAndIsCartAndProdId(req.getAccount(), body.isCart, body.getProdId());
		}

		if (cartProdList.size() > 0 && body.isCart()) {
			body.setQty(cartProdList.get(0).getQty() + body.getQty());
			body.setCartId(cartProdList.get(0).getCartId());
		}
		if (cartProdList.size() > 0 && body.isCart() == false) {
			cartList = getProductByCart(
					cartDao.findByAccountAndIsCartOrderByUpdateDtDesc(req.getAccount(), body.isCart));
			return OUT.builder().cartList(cartList).code(333).msg("execute success.").build();
		} else {
			Cart newCart = cartDao.save(bodyToCart(body, req));
			if (newCart != null) {
				cartList = getProductByCart(
						cartDao.findByAccountAndIsCartOrderByUpdateDtDesc(req.getAccount(), body.isCart));
				log.info("cartList:{}", cartList);
			} else {
				return OUT.builder().code(999).msg("新增或更新失敗").build();
			}
		}
		return OUT.builder().cartList(cartList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

	public OUT updateCart(BODY body, Request req) {
		List<Cart> cartList = new ArrayList<Cart>();
		Cart newCart = cartDao.save(bodyToCart(body, req));
		if (newCart != null) {
			cartList = getProductByCart(
					cartDao.findByAccountAndIsCartOrderByUpdateDtDesc(req.getAccount(), body.isCart));
			log.info("cartList:{}", cartList);
		} else {
			return OUT.builder().code(999).msg("新增或更新失敗").build();
		}
		return OUT.builder().cartList(cartList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}

	public Cart bodyToCart(BODY body, Request req) {
		ZonedDateTime taipeiTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Taipei"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String now = taipeiTime.format(formatter);
		return Cart.builder().account(req.getAccount()).cartId(body.getCartId()).isCart(body.isCart())
				.prodId(body.getProdId()).qty(body.getQty()).size(body.getSize()).updateDt(now).build();
	}

}
