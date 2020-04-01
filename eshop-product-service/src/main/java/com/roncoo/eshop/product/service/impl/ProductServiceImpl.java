package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.ProductMapper;
import com.roncoo.eshop.product.model.Product;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductServiceImpl implements ProductService {

	@Resource
	private ProductMapper productMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(Product product, String operationType) {
		productMapper.add(product);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"product\", \"id\": " + product.getId() + "}");
	}

	public void update(Product product, String operationType) {
		productMapper.update(product);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"product\", \"id\": " + product.getId() + "}");
	}

	public void delete(Long id, String operationType) {
		productMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"delete\", \"data_type\": \"product\", \"id\": " + id + "}");
	}

	public Product findById(Long id) {
		return productMapper.findById(id);
	}

}
