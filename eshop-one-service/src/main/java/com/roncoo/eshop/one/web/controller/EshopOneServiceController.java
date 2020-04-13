package com.roncoo.eshop.one.web.controller;

import com.roncoo.eshop.one.service.EshopInventoryService;
import com.roncoo.eshop.one.service.EshopPriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/one")  
public class EshopOneServiceController {

	@Resource
	private EshopInventoryService eshopInventoryService;
	@Resource
	private EshopPriceService eshopPriceService;
	
	@RequestMapping("/findInventoryByProductId")
	@ResponseBody
	public String findInventoryByProductId(Long productId) {
		return eshopInventoryService.findByProductId(productId);
	}
	
	@RequestMapping("/findPriceByProductId")
	@ResponseBody
	public String findPriceByProductId(Long productId) {
		return eshopPriceService.findByProductId(productId);
	}
	
}
