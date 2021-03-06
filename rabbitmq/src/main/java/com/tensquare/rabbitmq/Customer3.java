package com.tensquare.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author ysl
 * @Date 2019/11/21 22:47
 * @Description:
 **/

@Component
@RabbitListener(queues = "kudingyu")
public class Customer3 {

    @RabbitHandler
    public void showMessage(String message){
        System.out.println("kudingyu接收到消息："+message);
    }
}
