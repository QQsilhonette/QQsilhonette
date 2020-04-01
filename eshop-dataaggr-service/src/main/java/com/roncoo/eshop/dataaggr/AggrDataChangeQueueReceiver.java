package com.roncoo.eshop.dataaggr;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/1 2:40 下午
 * @description：MQ消费者
 */

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;

@Component
@RabbitListener(queues = "aggr-data-change-queue")
public class AggrDataChangeQueueReceiver {

    @Resource
    private JedisPool jedisPool;

    @RabbitHandler
    public void process(String message) {
        JSONObject messageJSONObject = JSONObject.parseObject(message);
        String dimType = messageJSONObject.getString("dim_type");

        switch(dimType) {
            case "brand":
                processBrandDimDataChange(messageJSONObject);
                break;
            case "category":
                processCategoryDimDataChange(messageJSONObject);
                break;
            case "product_intro":
                processProductIntroDimDataChange(messageJSONObject);
                break;
            case "product":
                processProductDimDataChange(messageJSONObject);
                break;

        }
    }

    private void processBrandDimDataChange(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");

        Jedis jedis = jedisPool.getResource();
        String dataJson = jedis.get("brand_" + id);

        if(dataJson != null && !"".equals(dataJson)) {
            jedis.set("dim_brand_" + id, dataJson);
        } else {
            jedis.del("dim_brand_" + id);
        }
    }

    private void processCategoryDimDataChange(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");

        Jedis jedis = jedisPool.getResource();
        String dataJSON = jedis.get("category_" + id);

        if(dataJSON != null && !"".equals(dataJSON)) {
            jedis.set("dim_category_" + id, dataJSON);
        } else {
            jedis.del("dim_category_" + id);
        }
    }

    private void processProductIntroDimDataChange(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");

        Jedis jedis = jedisPool.getResource();

        String dataJSON = jedis.get("product_intro_" + id);

        if(dataJSON != null && !"".equals(dataJSON)) {
            jedis.set("dim_product_intro_" + id, dataJSON);
        } else {
            jedis.del("dim_product_intro_" + id);
        }
    }

    private void processProductDimDataChange(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");

        Jedis jedis = jedisPool.getResource();

        List<String> result = jedis.mget("product_" + id, "product_property_" + id, "product_specification_" + id);
        String productDataJSON = result.get(0);

        if(productDataJSON != null && !"".equals(productDataJSON)) {
            JSONObject productDataJSONObject = JSONObject.parseObject(productDataJSON);

            String productPropertyDataJSON = result.get(1);
            if(productPropertyDataJSON != null && !"".equals(productPropertyDataJSON)) {
                productDataJSONObject.put("product_property", JSONObject.parse(productPropertyDataJSON));
            }

            String productSpecificationDataJSON = result.get(2);
            if(productSpecificationDataJSON != null && !"".equals(productSpecificationDataJSON)) {
                productDataJSONObject.put("product_specification", JSONObject.parse(productSpecificationDataJSON));
            }

            jedis.set("dim_product_" + id, productDataJSONObject.toJSONString());
        } else {
            jedis.del("dim_product_" + id);
        }

    }
}
