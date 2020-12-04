package com.tigers.amq.queue;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueueConsumer {

    private static Logger log = LoggerFactory.getLogger(QueueConsumer.class);

    @JmsListener(destination = "inbound.queue", selector = "JMSCorrelationID LIKE 'Correlation%'")
    @SendTo("outbound.queue")
    public String receiveMessage(@Payload final String jsonMessage, MessageHeaders messageHeaders) {
        String headers = messageHeaders.entrySet().stream()
                .map(entry -> " " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
        log.info("Received queue message:\n" + headers + "\n body: " + jsonMessage);
        Map map = new Gson().fromJson(jsonMessage, Map.class);
        return "Hello " + map.get("name");
    }

    @JmsListener(destination = "inbound.topic")
    @SendTo("outbound.topic")
    public Message<String> receiveMessageFromTopic(final String jsonMessage) {
        log.info("Received topic message " + jsonMessage);
        Map map = new Gson().fromJson(jsonMessage, Map.class);

        Map<String, String> headers = new HashMap();
        headers.put("date", "2000-01-01");
        headers.put("domain", "maas");
        headers.put("type", "instance");
        final String headerJson = new Gson().toJson(headers);

        return MessageBuilder.withPayload("Hello " + map.get("name"))
                .setHeader("header", headerJson)
                .build();
    }
}
