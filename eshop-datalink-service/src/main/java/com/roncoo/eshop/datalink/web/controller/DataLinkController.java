package com.roncoo.eshop.datalink.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.datalink.service.EshopProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/1 7:36 下午
 * @description：数据直连
 */
public class DataLinkController {

    @Resource
    private EshopProductService eshopProductService;
    @Resource
    private JedisPool jedisPool;

    @RequestMapping("/getProduct")
    @ResponseBody
    public String getProduct(Long productId) {
        //TODO 读取本地ehcache

        // 读redis主集群
        Jedis jedis = jedisPool.getResource();
        String dimProductJSON = jedis.get("dim_product_" + productId);

        if (dimProductJSON == null || "".equals(dimProductJSON)) {
            String productDataJSON = eshopProductService.findProductById(productId);

            if (productDataJSON == null || "".equals(productDataJSON)) {
                JSONObject productDataJSONObject = JSONObject.parseObject(productDataJSON);

                String productPropertyDataJSON = eshopProductService.findProductPropertyByProductId(productId);
                if (productPropertyDataJSON != null && !"".equals(productPropertyDataJSON)) {
                    productDataJSONObject.put("product_property", JSONObject.parse(productPropertyDataJSON));
                }

                String productSpecificationDataJSON = eshopProductService.findProductSpecificationByProductId(productId);
                if (productSpecificationDataJSON != null && !"".equals(productSpecificationDataJSON)) {
                    productDataJSONObject.put("product_specification", JSONObject.parse(productSpecificationDataJSON));
                }

                jedis.set("dim_product_" + productId, productDataJSONObject.toJSONString());

                return productDataJSONObject.toJSONString();
            }
        }

        return "";
    }

}
