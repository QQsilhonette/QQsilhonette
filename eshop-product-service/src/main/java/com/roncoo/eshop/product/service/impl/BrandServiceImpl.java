package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.product.mapper.BrandMapper;
import com.roncoo.eshop.product.model.Brand;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.rabbitmq.RabbitUtil;
import com.roncoo.eshop.product.service.BrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

	@Resource
	private BrandMapper brandMapper;
	@Resource
	private RabbitMQSender rabbitMQSender;
	
	public void add(Brand brand, String operationType) {
		brandMapper.add(brand);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"add\", \"data_type\": \"brand\", \"id\": " + brand.getId() + "}");
	}

	public void update(Brand brand, String operationType) {
		brandMapper.update(brand);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"update\", \"data_type\": \"brand\", \"id\": " + brand.getId() + "}");
	}

	public void delete(Long id, String operationType) {
		brandMapper.delete(id);
		rabbitMQSender.send(RabbitUtil.getQueue(operationType), "{\"event_type\": \"delete\", \"data_type\": \"brand\", \"id\": " + id + "}");
	}

	public Brand findById(Long id) {
		return brandMapper.findById(id);
	}

	public List<Brand> findByIds(String ids) {
		return brandMapper.findByIds(ids);
	}
}
