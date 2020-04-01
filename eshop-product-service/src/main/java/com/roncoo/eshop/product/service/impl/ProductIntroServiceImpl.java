package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.ProductIntroMapper;
import com.roncoo.eshop.product.model.ProductIntro;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.ProductIntroService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductIntroServiceImpl implements ProductIntroService {

	@Resource
	private ProductIntroMapper productIntroMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(ProductIntro productIntro, String operationType) {
		productIntroMapper.add(productIntro);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"product_intro\", \"id\": " + productIntro.getId() + ", \"product_id\": " + productIntro.getProductId() + "}");
	}

	public void update(ProductIntro productIntro, String operationType) {
		productIntroMapper.update(productIntro);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"product_intro\", \"id\": " + productIntro.getId() + ", \"product_id\": " + productIntro.getProductId() + "}");
	}

	public void delete(Long id, String operationType) {
		ProductIntro productIntro = findById(id);
		productIntroMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"delete\", \"data_type\": \"product_intro\", \"id\": " + id + ", \"product_id\": " + productIntro.getProductId() + "}");
	}

	public ProductIntro findById(Long id) {
		return productIntroMapper.findById(id);
	}

}
