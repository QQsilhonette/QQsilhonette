package com.roncoo.eshop.one.service.fallback;

import com.roncoo.eshop.one.service.EshopInventoryService;
import org.springframework.stereotype.Component;

@Component
public class EshopInventoryServiceFallback implements EshopInventoryService {
	
	public String findByProductId(Long productId) {
		return "降级库存数据";
	}

}
