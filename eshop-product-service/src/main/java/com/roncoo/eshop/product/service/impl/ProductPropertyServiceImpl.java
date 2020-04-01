package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.ProductPropertyMapper;
import com.roncoo.eshop.product.model.ProductProperty;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.ProductPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductPropertyServiceImpl implements ProductPropertyService {

	@Resource
	private ProductPropertyMapper productPropertyMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(ProductProperty productProperty, String operationType) {
		productPropertyMapper.add(productProperty);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"product_property\", \"id\": " + productProperty.getId() + ", \"product_id\": " + productProperty.getProductId() + "}");
	}

	public void update(ProductProperty productProperty, String operationType) {
		productPropertyMapper.update(productProperty);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"product_property\", \"id\": " + productProperty.getId() + ", \"product_id\": " + productProperty.getProductId() + "}");
	}

	public void delete(Long id, String operationType) {
		ProductProperty productProperty = findById(id);
		productPropertyMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"delete\", \"data_type\": \"product_property\", \"id\": " + id + ", \"product_id\": " + productProperty.getProductId() + "}");
	}

	public ProductProperty findById(Long id) {
		return productPropertyMapper.findById(id);
	}

	public ProductProperty findByProductId(Long productId) {
		return productPropertyMapper.findByProductId(productId);
	}
}
