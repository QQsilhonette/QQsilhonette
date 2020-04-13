package com.roncoo.eshop.price.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.price.mapper.ProductPriceMapper;
import com.roncoo.eshop.price.model.ProductPrice;
import com.roncoo.eshop.price.service.ProductPriceService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {

	@Resource
	private ProductPriceMapper productPriceMapper;
	@Resource
	private JedisPool jedisPool;
	
	public void add(ProductPrice productPrice) {
		productPriceMapper.add(productPrice);
		Jedis jedis = jedisPool.getResource();
		jedis.set("product_price_" + productPrice.getProductId(), JSONObject.toJSONString(productPrice));
	}

	public void update(ProductPrice productPrice) {
		productPriceMapper.update(productPrice);
		Jedis jedis = jedisPool.getResource();
		jedis.set("product_price_" + productPrice.getProductId(), JSONObject.toJSONString(productPrice));
	}

	public void delete(Long id) {
		ProductPrice productPrice = findById(id);
		productPriceMapper.delete(id);
		Jedis jedis = jedisPool.getResource();
		jedis.del("product_price_" + productPrice.getProductId());
	}

	public ProductPrice findById(Long id) {
		return productPriceMapper.findById(id);
	}

	public ProductPrice findByProductId(Long productId) {
		Jedis jedis = jedisPool.getResource();
		String dataJSON = jedis.get("product_price_" + productId);
		if(dataJSON != null && !"".equals(dataJSON)) {
			JSONObject dataJsonObject = JSONObject.parseObject(dataJSON);
			dataJsonObject.put("id", "-1");
			return dataJsonObject.parseObject(dataJsonObject.toJSONString(), ProductPrice.class);
		} else {
			return productPriceMapper.findByProductId(productId);
		}
	}
}
