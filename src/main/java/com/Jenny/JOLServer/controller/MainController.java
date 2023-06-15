package com.Jenny.JOLServer.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.common.Fun;
import com.Jenny.JOLServer.fun.JOLCartInfo;
import com.Jenny.JOLServer.fun.JOLCustomerInfo;
import com.Jenny.JOLServer.fun.JOLOrderDetailInfo;
import com.Jenny.JOLServer.fun.JOLOrderInfo;
import com.Jenny.JOLServer.fun.JOLProductInfo;
import com.Jenny.JOLServer.fun.LogIn;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.model.StripePay;
import com.Jenny.JOLServer.tool.CustomException;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

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


	@PostMapping("/json")
	public Object Dispatcher(@RequestBody Request req) throws Exception {
		if (req.getFun().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: fun");
		}
		if (req.getAccount().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: accounts");
		}
		if (!"LogIn".equals(req.getFun()) && req.getType().isEmpty()) {
			throw new CustomException("PARAM NOT FOUND: type");
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
		}
		return out;
	}


	@PostMapping("/payment")
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
