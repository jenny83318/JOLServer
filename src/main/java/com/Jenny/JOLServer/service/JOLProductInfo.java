package com.Jenny.JOLServer.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Jenny.JOLServer.common.Type;
import com.Jenny.JOLServer.dao.ProductInfoDao;
import com.Jenny.JOLServer.model.Product;
import com.Jenny.JOLServer.model.Request;
import com.Jenny.JOLServer.tool.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Service
public class JOLProductInfo {
	private static final Logger log = LoggerFactory.getLogger(JOLProductInfo.class);
	@Autowired
	private ProductInfoDao productDao;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BODY {
		private String type;
		private Integer prodId;
		private String name;
		private String description;
		private String imgUrl;
		private String updateDt;
		private String createDt;
		private Integer price;
		private Integer cost;
		private Integer qty;
		private String category;
		private String size;
	}
	
	@Data
	@Builder
	public static class OUT {
		private Integer code;
		private String msg;
		private List<Prod> productList;
	}
	
	@Data
	@Builder
	public static class Prod {
		private Integer prodId;
		private String name;
		private String description;
		private List<String> byteArrayList;
		private String updateDt;
		private String createDt;
		private Integer price;
		private Integer cost;
		private Integer qty;
		private String category;
		private String size;
	}
	
	protected Request check(Request req) throws Exception {
		if (req.getBody().get("type") == null) {
			throw new CustomException("PARAM NOT FOUND: type");
		}
		if (!"ALL".equals(req.getBody().get("type"))) {
			if (req.getBody().get("cartId") == null) {
				throw new CustomException("PARAM NOT FOUND: prodId");
			}
		}
		if ("ADD".equals(req.getBody().get("type")) || "UPDATE".equals(req.getBody().get("type"))) {
			if (req.getBody().get("name") == null) {
				throw new CustomException("PARAM NOT FOUND: name");
			}
			if (req.getBody().get("description") == null) {
				throw new CustomException("PARAM NOT FOUND: qty");
			}
			if (req.getBody().get("imgUrl") == null) {
				throw new CustomException("PARAM NOT FOUND: size");
			}
			if (req.getBody().get("updateDt") == null) {
				throw new CustomException("PARAM NOT FOUND: updateDt");
			}
			if (req.getBody().get("createDt") == null) {
				throw new CustomException("PARAM NOT FOUND: createDt");
			}
			if (req.getBody().get("price") == null) {
				throw new CustomException("PARAM NOT FOUND: price");
			}
			if (req.getBody().get("cost") == null) {
				throw new CustomException("PARAM NOT FOUND: cost");
			}
			if (req.getBody().get("qty") == null) {
				throw new CustomException("PARAM NOT FOUND: qty");
			}
			if (req.getBody().get("category") == null) {
				throw new CustomException("PARAM NOT FOUND: category");
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
		List<Product> productList = new ArrayList<Product>();
		List<Prod> prodList = new ArrayList<Prod>();
		Type type = Type.getType(body.getType());
		switch(type) {
		case ALL:
			productList = productDao.findAll();
			for(Product p : productList ) {
				prodList.add(ProdcutToProd(p));
			}
			break;
		case SELECT:
			Product p = productDao.findByProdId(body.getProdId());
			if(p != null) {
				prodList.add(ProdcutToProd(p));
			}
			break;
		case ADD:
		case UPDATE:
			Product prod = productDao.save(BodyToProduct(body));
			if(prod != null) {
				prodList.add(ProdcutToProd(prod));
			}
			break;
		case DELETE:
			productDao.deleteById(body.getProdId());
			break;
		default:
			break;
		}
		log.info("prodList:{}", prodList);
		return OUT.builder().productList(prodList).code(HttpStatus.OK.value()).msg("execute success.").build();
	}
	
	
	public List<String> getImg(String url) throws IOException{
		List<String> byteArrayList = new ArrayList<String>();
		List<String> urlList = Arrays.asList(url.split(","));
		for(String urlStr : urlList) {
			log.info("url:{}",urlStr);
			InputStream inputStream = null;
			byte[] imgBytes = null;
			try {
				inputStream = getClass().getResourceAsStream(urlStr);
				imgBytes = IOUtils.toByteArray(inputStream);
				byteArrayList.add(Base64.getEncoder().encodeToString(imgBytes));
			} catch (IOException e) {
				throw new IOException("GET IMG ERROR:{}" + e.getMessage());
			}finally {
				inputStream.close();
			}
		}
		return byteArrayList;
	}
	
	public Prod ProdcutToProd(Product p) throws IOException {
		return Prod.builder()
				.prodId(p.getProdId())
				.cost(p.getCost())
				.price(p.getPrice())
				.qty(p.getQty())
				.byteArrayList(getImg(p.getImgUrl()))
				.category(p.getCategory())
				.createDt(p.getCreateDt()) 
				.description(p.getDescript())
				.name(p.getName())
				.size(p.getSize())
				.updateDt(p.getUpdateDt()).build();
	}
	
	public Product BodyToProduct(BODY body) {
		return Product.builder()
				.category(body.getCategory())
				.cost(body.getCost())
				.createDt(body.getCreateDt())
				.descript(body.getDescription())
				.imgUrl(body.getImgUrl())
				.name(body.getName())
				.price(body.getPrice())
				.prodId(body.getProdId())
				.qty(body.getQty())
				.size(body.getSize())
				.updateDt(body.getUpdateDt()).build();
	}
}
