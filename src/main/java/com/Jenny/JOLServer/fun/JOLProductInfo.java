package com.Jenny.JOLServer.fun;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
/**
 * {
    "fun": "JOLProductInfo",
    "account": "jenny83318",
    "type": "ALL",
    "body": {}
}
 * @author Jenny
 *
 */
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
        private String selectType;
        private String keyWord;
        private Integer prodId;
        private String name;
        private String description;
        private String imgUrl;
        private ZonedDateTime updateDt;
        private ZonedDateTime createDt;
        private Integer price;
        private Integer cost;
        private Integer qty;
        private String category;
        private String series;
        private String size;
    }

    @Data
    @Builder
    public static class OUT {
        private Integer code;
        private String msg;
        private List<Product> productList;
    }

    private void check(Request req) {
        Field[] fields = BODY.builder().build().getClass().getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            Object value = req.getBody().get(key);
            if (!"ALL".equals(req.getType()) && !"OTHER".equals(req.getType()) && "prodId".equals(key) && value == null) {
                log.error("PARAM NOT FOUND: {}", key);
                throw new CustomException("PARAM NOT FOUND: " + key);
            }
            if ("OTHER".equals(req.getType())) {
                if (("keyWord".equals(key) || "selectType".equals(key)) && value == null) {
                    log.error("PARAM NOT FOUND: {}", key);
                    throw new CustomException("PARAM NOT FOUND: " + key);
                }
            }
            if (("ADD".equals(req.getType()) || "UPDATE".equals(req.getType())) && !"keyWord".equals(key) && !"prodId".equals(key)) {
                if (value == null) {
                    log.error("PARAM NOT FOUND: {}", key);
                    throw new CustomException("PARAM NOT FOUND: " + key);
                }
            }
        }
    }

    public BODY parser(Map<String, Object> map) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(map, BODY.class);
    }

    public OUT doProcess(Request req) {
        check(req);
        BODY body = parser(req.getBody());
        List<Product> productList = new ArrayList<>();
        Type type = Type.getType(req.getType());
        switch (Objects.requireNonNull(type)) {
            case ALL:
                productList = productDao.findAll();
                break;
            case SELECT:
                Product p = productDao.findByProdId(body.getProdId());
                productList.add(p);
                break;
            case OTHER:
                if ("keyWord".equals(body.getSelectType())) {
                    productList = productDao.findByNameContaining(body.getKeyWord());
                } else if ("category".equals(body.getSelectType())) {
                    productList = productDao.findByCategory(body.getKeyWord());
                } else if ("series".equals(body.getSelectType())) {
                    productList = productDao.findBySeries(body.getKeyWord());
                }
                log.info("body.getSelectType :{}", body.getSelectType());
                break;
            case ADD:
            case UPDATE:
                Product updateProd = productDao.save(BodyToProduct(body));
                productList.add(updateProd);
                break;
            case DELETE:
                productDao.deleteById(body.getProdId());
                break;
            default:
                break;
        }

        return OUT.builder().productList(productList).code(HttpStatus.OK.value()).msg("execute success.").build();
    }

    /*圖片改直接從雲端取
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
    */
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
                .sizeInfo(body.getSize())
                .updateDt(body.getUpdateDt()).build();
    }
}
