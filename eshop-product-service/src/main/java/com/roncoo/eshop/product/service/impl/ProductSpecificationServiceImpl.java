package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.ProductSpecificationMapper;
import com.roncoo.eshop.product.model.ProductSpecification;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.ProductSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

	@Resource
	private ProductSpecificationMapper productSpecificationMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(ProductSpecification productSpecification, String operationType) {
		productSpecificationMapper.add(productSpecification);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"product_specification\", \"id\": " + productSpecification.getId() + ", \"product_id\": " + productSpecification.getProductId() + "}");
	}

	public void update(ProductSpecification productSpecification, String operationType) {
		productSpecificationMapper.update(productSpecification);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"product_specification\", \"id\": " + productSpecification.getId() + ", \"product_id\": " + productSpecification.getProductId() + "}");
	}

	public void delete(Long id, String operationType) {
		ProductSpecification productSpecification = findById(id);
		productSpecificationMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"delete\", \"data_type\": \"product_specification\", \"id\": " + id + ", \"product_id\": " + productSpecification.getProductId() + "}");
	}

	public ProductSpecification findById(Long id) {
		return productSpecificationMapper.findById(id);
	}

	public ProductSpecification findByProductId(Long productId) {
		return productSpecificationMapper.findById(productId);
	}
}
