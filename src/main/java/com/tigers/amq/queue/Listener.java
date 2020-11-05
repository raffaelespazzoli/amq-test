package com.tigers.amq.queue;

import com.google.gson.Gson;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Listener {

    @JmsListener(destination = "inbound.queue")
    @SendTo("outbound.queue")
    public String receiveMessage(final String jsonMessage) {
        System.out.println("Received queue message " + jsonMessage);
        Map map = new Gson().fromJson(jsonMessage, Map.class);
        return "Hello " + map.get("name");
    }

    @JmsListener(destination = "inbound.topic")
    @SendTo("outbound.topic")
    public String receiveMessageFromTopic(final String jsonMessage) {
        System.out.println("Received topic message " + jsonMessage);
        Map map = new Gson().fromJson(jsonMessage, Map.class);
        return "Hello " + map.get("name");
    }
}
