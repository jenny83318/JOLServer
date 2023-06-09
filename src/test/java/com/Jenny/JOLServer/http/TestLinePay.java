package com.Jenny.JOLServer.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLinePay {
	@Autowired
	private LinePay linePay;

	@Test
	public void TestSplit() throws Exception  {
		String body = "{\"prime\":prime,\"partner_key\":partnerKey,\"merchant_id\":\"jenny83318_LINEPAY\",\"details\":\"TapPayTest\",\"amount\":1000,\"cardholder\":{\"phone_number\":\"+886911788163\",\"name\":\"王小明\",\"email\":\"jenny83318@gmail.com\",\"zip_code\":\"100\",\"address\":\"台北市天龍區芝麻街1號1樓\",\"national_id\":\"A123456789\"},\"remember\":true}";
		log.info("{}", linePay.call(body));

	}
}
