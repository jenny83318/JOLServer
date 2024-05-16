package com.Jenny.JOLServer.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.common.Fun;
import com.Jenny.JOLServer.fun.JOLCartInfo;
import com.Jenny.JOLServer.fun.JOLCustomerInfo;
import com.Jenny.JOLServer.fun.JOLEmailInfo;
import com.Jenny.JOLServer.fun.JOLOrderDetailInfo;
import com.Jenny.JOLServer.fun.JOLOrderInfo;
import com.Jenny.JOLServer.fun.JOLProductInfo;
import com.Jenny.JOLServer.fun.LogIn;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.Data;

@RequestMapping("/api")
@RestController
public class MainController {
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	private static Gson gson = new Gson();
	@Autowired()
	private LogIn logIn;

	@Autowired()
	private JOLCustomerInfo custService;

	@Autowired()
	private JOLCartInfo cartService;

	@Autowired()
	private JOLProductInfo prodService;

	@Autowired()
	private JOLOrderInfo orderService;

	@Autowired()
	private JOLOrderDetailInfo detailService;
	
	@Autowired()
	private JOLEmailInfo emailService;

	@Data
	private static class StripePay {
		private String name;
		private String currency;
		private String successUrl;
		private String cancelUrl;
		private long amount;
		private long quantity;
	}

	@PostMapping("/json")
	@CrossOrigin(origins = "https://jol-boutique.onrender.com")
	public Object Dispatcher(@RequestBody Request req) throws Exception {
		log.info("REQ:{}",req);
		if (req.getFun().isEmpty()) {
			log.error("PARAM NOT FOUND: fun");
			throw new CustomException("PARAM NOT FOUND: fun");
		}
		if (!"JOLProductInfo".equals(req.getFun()) && req.getAccount().isEmpty()) {
			log.error("PARAM NOT FOUND: account");
			throw new CustomException("PARAM NOT FOUND: account");
		}
		if (!"LogIn".equals(req.getFun()) && req.getType().isEmpty()) {
			log.error("PARAM NOT FOUND: type");
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if (!"LogIn".equals(req.getFun()) && !"JOLProductInfo".equals(req.getFun()) && req.getToken().isEmpty()
				&& ! ("JOLCustomerInfo".equals(req.getFun()) && "ADD".equals(req.getType()))) {
			log.error("PARAM NOT FOUND: token");
			throw new CustomException("PARAM NOT FOUND: token");
		}
		if(!"LogIn".equals(req.getFun()) && !"JOLProductInfo".equals(req.getFun())
			&& ! ("JOLCustomerInfo".equals(req.getFun()) && "ADD".equals(req.getType()))) {
			LogIn.OUT c = this.logIn.checkToken(req.getAccount(), req.getToken());
			if(c.getCode() != 200) {
				return c;
			}
		}
		Object out = new Object();
		Fun fun = Fun.getFunValue(req.getFun());
		switch (fun) {
		case LOGIN:
			out = logIn.doProcess(req);
			break;
		case CUSTOMER:
			out = custService.doProcess(req);
			break;
		case CART:
			out = cartService.doProcess(req);
			break;
		case PRODUCT:
			out = prodService.doProcess(req);
			break;
		case ORDER:
			out = orderService.doProcess(req);
			break;
		case ORDERDETAIL:
			out = detailService.doProcess(req);
			break;
		case EMAIL:
			out = emailService.doProcess(req);
		}
		log.info("RES:{}",out);
		return out;
	}


	@PostMapping("/payment")
	@CrossOrigin(origins = "https://jol-boutique.onrender.com")
	public String paymentWithCheckoutPage(@RequestBody StripePay payment) throws StripeException {
		init();
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
				.setCancelUrl(payment.getCancelUrl())
				.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
								.setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency(payment.getCurrency())
								.setUnitAmount(payment.getAmount()).setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(payment.getName()).build()).build()).build()).build();
		Session session = Session.create(params);
		Map<String, String> responseData = new HashMap<>();
		responseData.put("id", session.getId());
		return gson.toJson(responseData);
	}

	private static void init() {
		Stripe.apiKey = "sk_test_51NIikVB9Nt3grzHayr7dFS4rbsKmqvr3q6AqICiH2uS3xtbx9lG8Ftynbyu22AJ8SZWpchqqAVBZYJw3W82Za2oO00B2VEJovm";
	}
}
