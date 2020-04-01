package com.roncoo.eshop.product.rabbitmq;

/**
 * @author ：luoqi/02216
 * @date ：Created in 2020/4/1 5:39 下午
 * @description：mq公共方法
 */

public class RabbitUtil {

    public static String getQueue(String operationType) {
        String result = null;
        if (operationType == null || "".equals(operationType)) {
            result = RabbitQueue.DATA_CHANGE_QUEUE;
        } else if ("refresh".equals(operationType)) {
            result = RabbitQueue.REFRESH_DATA_CHANGE_QUEUE;
        } else if ("high".equals(operationType)) {
            result = RabbitQueue.HIGH_PRIORITY_DATA_CHANGE_QUEUE;
        }
        return result;
    }
}
