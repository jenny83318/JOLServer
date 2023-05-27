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
		private String type;
		private String size;
		private boolean isCart;
	}

	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private List<CartItem> cartList;
	}
	

	@Data
	@Builder
	public static class CartItem {
		private Integer cartId;
		private Integer prodId;
		private String account;
		private Integer qty;
		private Integer price;
		private String name;
		private String imgUrl;
		private String size;
		private String updateDt;
		private boolean isCart;
	}

	protected Request check(Request req) throws Exception {
		log.info("req.getBody().get(prodId):{}",req.getBody().get("prodId"));
		if (req.getAccount().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: account");
		}
		if (req.getBody().get("type") == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if (!"ALL".equals(req.getBody().get("type")) && req.getBody().get("isCart") == null) {
			throw new CustomException("PARAM NOT FOUND: isCart");
		}
		if (!"ALL".equals(req.getBody().get("type")) && !"SELECT".equals(req.getBody().get("type")) && ! "ADD".equals(req.getBody().get("type"))) {
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
//		List<Cart> cartList = new ArrayList<Cart>();
		List<CartItem> cartList = new ArrayList<CartItem>();
		Type type = Type.getType(body.getType());
		switch (type) {
		case ALL:
			cartList = getCartDataByAccount(req.getAccount(), body.isCart, "ALL");
			break;
		case SELECT:
			cartList = getCartDataByAccount(req.getAccount(), body.isCart, "SELECT");
			break;
		case ADD:
		case UPDATE:
			LocalDateTime currentTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	        String now = currentTime.format(formatter);
	        List<Cart> cartProdList = cartDao.findByAccountAndIsCartAndProdIdAndSize(req.getAccount(), body.isCart, body.getProdId(),body.getSize());
	        if(cartProdList.size() > 0) {
	        	body.setQty(cartProdList.get(0).getQty() + body.getQty());
	        	body.setCartId(cartProdList.get(0).getCartId());
	        }
			Cart newCart = cartDao.save(
					Cart.builder()
					.account(req.getAccount())
					.cartId(body.getCartId())
					.isCart(body.isCart)
					.prodId(body.getProdId())
					.qty(body.getQty())
					.size(body.getSize())
					.updateDt(now).build());
			if (newCart != null) {
				cartList =  getCartDataByAccount(req.getAccount(), body.isCart,"SELECT");
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
	
	public List<CartItem> getCartDataByAccount(String account, boolean isCart, String type) {
		List<CartItem> cartItemList = new ArrayList<>();
		List<Cart> cartList = new ArrayList<>();
		if("SELECT".equals(type)) {
		cartList = cartDao.findByAccountAndIsCart(account, isCart);
		}
		if("ALL".equals(type)) {
		 cartList = cartDao.findByAccount(account);
		}
		for(Cart c : cartList) {
			Product p = productDao.findByProdId(c.getProdId());				
			cartItemList.add(CartItem.builder().cartId(c.getCartId()).prodId(c.getProdId()).qty(c.getQty())
					.account(c.getAccount()).size(c.getSize()).isCart(c.isCart()).updateDt(c.getUpdateDt())
					.price(p.getPrice()).imgUrl(p.getImgUrl()).name(p.getName()).build());
		}
		return cartItemList;
		
	}
}
