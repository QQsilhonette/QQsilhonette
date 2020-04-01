package com.roncoo.eshop.datasync.rabbitmq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.datasync.service.EshopProductService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据同步服务，获取各种原子数据的变更消息
 *
 * （1）然后通过Spring cloud feign调用eshop-product-service服务的各种接口，获取数据
 * （2）将原子数据在redis中进行增删改
 * （3）将维度数据变化消息写入rabbitmq中另外一个queue，供数据聚合服务来消费
 *
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/1 11:33 上午
 * @description：mq消息消费者
 */

@Component
@RabbitListener(queues = "data-change-queue")
public class DataChangeQueueReceiver {

    @Resource
    private EshopProductService eshopProductService;
    @Resource
    private JedisPool jedisPool;
    @Resource
    private RabbitMQSender rabbitMQSender;

    private Set<String> dimDataChangeMessageSet = Collections.synchronizedSet(new HashSet<String>());

    public DataChangeQueueReceiver() {
        new SendThread().start();
    }

    private List<JSONObject> brandDataChangeMessageList = new ArrayList<>();

    @RabbitHandler
    public void process(String message) {
        // 对这个message进行解析
        JSONObject jsonObject = JSONObject.parseObject(message);

        //先获取data_type
        String dataType = jsonObject.getString("data_type");
        switch (dataType) {
            case "brand":
                processBrandDataChangeMessage(jsonObject);
                break;
            case "category":
                processCategoryDataChangeMessage(jsonObject);
                break;
            case "product_intro":
                processProductIntroDataChangeMessage(jsonObject);
                break;
            case "product_property":
                processProductPropertyDataChangeMessage(jsonObject);
                break;
            case "product":
                processProductDataChangeMessage(jsonObject);
                break;
            case "product_specification":
                processProductSpecificationDataChangeMessage(jsonObject);
                break;
            default:
                break;
        }
    }

    private void processBrandDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            brandDataChangeMessageList.add(messageJSONObject);

            System.out.println("【将品牌数据放入内存list中】,list.size=" + brandDataChangeMessageList.size());

            if(brandDataChangeMessageList.size() >= 2) {
                System.out.println("【将品牌数据内存list大小大于等于2，开始执行批量调用】");

                String ids ="";

                for(int i = 0; i < brandDataChangeMessageList.size(); i++) {
                    ids += brandDataChangeMessageList.get(i).getLong("id");
                    if(i < brandDataChangeMessageList.size() - 1) {
                        ids += ",";
                    }
                }

                System.out.println("【品牌数据ids生成】ids=" + ids);

                JSONArray brandJSONArray = JSONArray.parseArray(eshopProductService.findBrandByIds(ids));

                System.out.println("【通过批量调用获取到品牌数据】jsonArray=" + brandJSONArray.toJSONString());

                for(int i = 0; i < brandJSONArray.size(); i++) {
                    JSONObject dataJSONObject = brandJSONArray.getJSONObject(i);

                    Jedis jedis = jedisPool.getResource();
                    jedis.set("brand_" + dataJSONObject.getLong("id"), dataJSONObject.toJSONString());

                    System.out.println("【将品牌数据写入redis】brandId=" + dataJSONObject.getLong("id"));

                    dimDataChangeMessageSet.add("{\"dim_type\": \"brand\", \"id\": " + dataJSONObject.getLong("id") + "}");

                    System.out.println("【将品牌数据写入内存去重set中】brandId=" + dataJSONObject.getLong("id"));
                }

                brandDataChangeMessageList.clear();
            }
        } else if("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("_brand" + id);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"brand\", \"id\": " + id + "}");
    }

    private void processCategoryDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findCategoryById(id));
            Jedis jedis = jedisPool.getResource();
            jedis.set("category_" + dataJSONObject.getLong("id"), dataJSONObject.toJSONString());
        } else if ("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("category_" + id);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"category\", \"id\": " + id + "}");
    }

    private void processProductIntroDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        Long productId = messageJSONObject.getLong("product_id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findProductIntroById(id));
            Jedis jedis = jedisPool.getResource();
            jedis.set("product_intro_" + productId, dataJSONObject.toJSONString());
        } else if ("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("product_intro_" + productId);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"category\", \"id\": " + productId + "}");
    }

    private void processProductDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findProductById(id));
            Jedis jedis = jedisPool.getResource();
            jedis.set("product_" + id, dataJSONObject.toJSONString());
        } else if ("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("product_" + id);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"product\", \"id\": " + id + "}");
    }

    private void processProductPropertyDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        Long productId = messageJSONObject.getLong("product_id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findProductPropertyById(id));
            Jedis jedis = jedisPool.getResource();
            jedis.set("product_property_" + productId, dataJSONObject.toJSONString());
        } else if ("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("product_property_" + productId);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"product\", \"id\": " + productId + "}");
    }

    private void processProductSpecificationDataChangeMessage(JSONObject messageJSONObject) {
        Long id = messageJSONObject.getLong("id");
        Long productId = messageJSONObject.getLong("product_id");
        String eventType = messageJSONObject.getString("event_type");

        if("add".equals(eventType) || "update".equals(eventType)) {
            JSONObject dataJSONObject = JSONObject.parseObject(eshopProductService.findProductSpecificationById(id));
            Jedis jedis = jedisPool.getResource();
            jedis.set("product_specification_" + productId, dataJSONObject.toJSONString());
        } else if ("delete".equals(eventType)) {
            Jedis jedis = jedisPool.getResource();
            jedis.del("product_specification_" + productId);
        }
        dimDataChangeMessageSet.add("{\"dim_type\": \"product\", \"id\": " + productId + "}");
    }

    private class SendThread extends Thread {

        @Override
        public void run() {
            while(true) {
                if(!dimDataChangeMessageSet.isEmpty()) {
                    for(String message : dimDataChangeMessageSet) {
                        rabbitMQSender.send("aggr-data-change-queue", message);
                    }
                    dimDataChangeMessageSet.clear();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
