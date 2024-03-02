package com.Jenny.JOLServer.fun;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.dao.OrderInfoDao;
import com.Jenny.JOLServer.model.Order;
import com.Jenny.JOLServer.model.OrderDetail;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Service
public class JOLEmailInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLEmailInfo.class);

	private final JavaMailSender mailSender;
	
	@Autowired()
	public JOLEmailInfo (JavaMailSender mailSender ) {
		this.mailSender = mailSender;
	}
	@Autowired()
	private OrderInfoDao orderDao;
	
	
	@Autowired()
	private JOLOrderDetailInfo detailService;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private String toEmail;
		private String subject;
		private String content1;
		private String content2;
		private String content3;
		private int orderNo;
	}
	
	@Data
	@Builder
	public static class OUT {
		@Builder.Default
		private int code = 0;
		@Builder.Default
		private String msg = "";
	}
	
	protected void check(Request req)  {
		Field[] fields = BODY.builder().build().getClass().getDeclaredFields();
		for (Field field : fields) {
			String key = field.getName();
			Object value = req.getBody().get(key);
			if(value == null) {				
				log.error("PARAM NOT FOUND: {}",key);
				throw new CustomException("PARAM NOT FOUND: " + key);
			}
		}
	}

	
	public BODY parser(Map<String, Object> map) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(map, BODY.class);
	}
	
	public OUT doProcess(Request req) throws Exception{
		log.info("SEND EMAIL REQ:{}" , req);
		BODY body = parser(req.getBody());
		Order o = orderDao.findByOrderNoAndAccount(body.getOrderNo(), req.getAccount());
		String content1 = getString(o, body);
		Map<String, Object> detailBody = new HashMap<>();
		detailBody.put("orderNo", o.getOrderNo());
		detailBody.put("account", o.getAccount());
		Request request = Request.builder().account(req.getAccount()).type("SELECT").fun("JOLOrderDetailInfo").body(detailBody).build();
		List<OrderDetail> detailList = detailService.doProcess(request).getDetailList();
		StringBuilder content2 = new StringBuilder();
		for(OrderDetail detail : detailList) {
			log.info("detail:{}",detail);
			String oriContent2 = body.getContent2();
			String[] img = detail.getImgUrl().split(",");
			log.info("imgURL:{}", img[0] );
			content2.append(oriContent2
                    .replaceAll("#ProdNo", String.format("%05d", detail.getProdId()))
                    .replaceAll("#ProdName", detail.getProdName())
                    .replaceAll("#ProdImg", img[0])
                    .replaceAll("#Size", detail.getSize())
                    .replaceAll("#Qty", Integer.toString(detail.getQty()))
                    .replaceAll("#SubTotal", Integer.toString(detail.getQty() * detail.getPrice()))
                    .replaceAll("#Price", Integer.toString(detail.getPrice())));
		}
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
		helper.setTo(body.getToEmail());
		helper.setSubject(body.getSubject());
		helper.setText(content1 + content2 + body.getContent3(), true);
		helper.setFrom(new InternetAddress("jolboutique12@gmail.com","JOL Boutique"));
		File image = new File("src/main/resources/image/JOLBoutique-logo.png");
		helper.addInline( "image", image);
		mailSender.send(message);
		log.info("SEND EMAIL response==========>" );
		return OUT.builder().code(HttpStatus.OK.value()).msg("execute success.").build();
	}

	private static String getString(Order o, BODY body) {
		String orderNo = String.format("%05d", o.getOrderNo());
		String orderDate = o.getOrderTime().substring(0,10);
		return body.getContent1()
				.replaceAll("#OrderNo", orderNo)
				.replaceAll("#Status", o.getStatus())
				.replaceAll("#PayBy", o.getPayBy())
				.replaceAll("#Shipment", o.getDeliveryWay())
				.replaceAll("#DeliveryFee", Integer.toString("7-11超商取貨".equals(o.getDeliveryWay()) ? 60 : "宅配".equals(o.getDeliveryWay())  ? 80 : 0))
				.replaceAll("#OrderAmt", Integer.toString(o.getTotalAmt()))
				.replaceAll("#OrderDate", orderDate);
	}

}
