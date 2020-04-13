package com.roncoo.eshop.one.service.fallback;

import com.roncoo.eshop.one.service.EshopPriceService;
import org.springframework.stereotype.Component;

@Component
public class EshopPriceServiceFallback implements EshopPriceService {

	public String findByProductId(Long productId) {
		return "降级价格数据";
	}

}
