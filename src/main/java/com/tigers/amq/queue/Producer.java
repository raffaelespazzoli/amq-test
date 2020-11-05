package com.tigers.amq.queue;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Producer {

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(final String queueName, final String message) {
        Map map = new Gson().fromJson(message, Map.class);
        final String textMessage = "Hello " + map.get("name");
        System.out.println("Sending message " + textMessage + " to queue - " + queueName);
        jmsTemplate.convertAndSend(queueName, textMessage);
    }

}