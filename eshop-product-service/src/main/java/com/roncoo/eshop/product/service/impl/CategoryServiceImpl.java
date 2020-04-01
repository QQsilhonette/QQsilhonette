package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.CategoryMapper;
import com.roncoo.eshop.product.model.Category;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryMapper categoryMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(Category category, String operationType) {
		categoryMapper.add(category);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"category\", \"id\": " + category.getId() + "}");
	}

	public void update(Category category, String operationType) {
		categoryMapper.update(category);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"category\", \"id\": " + category.getId() + "}");
	}

	public void delete(Long id, String operationType) {
		categoryMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"category\", \"id\": " + id + "}");
	}

	public Category findById(Long id) {
		return categoryMapper.findById(id);
	}

}
