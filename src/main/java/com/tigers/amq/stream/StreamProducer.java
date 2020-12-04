package com.tigers.amq.stream;

import com.google.gson.Gson;
import com.tigers.amq.rest.StreamDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StreamProducer {

    private static Logger log = LoggerFactory.getLogger(StreamProducer.class);

    @Autowired
    private EventChannels channels;

    public void sendMessage(StreamDestination streamDestination, final String message) {
        final String correlationID = "CorrelationID";
        Map<String, String> headers = new HashMap();
        headers.put("date", "2000-01-01");
        headers.put("domain", "maas");
        headers.put("type", "instance");
        final String headerJson = new Gson().toJson(headers);

        log.info("Sending message [" + message + "] to - " + streamDestination.streamName);
        Message<String> streamMessage = MessageBuilder.withPayload(message)
                .setHeader("correlationID", correlationID)
                .setHeader("event-type", "prefix.key1.key2.route")
                .setHeader("header", headerJson)
                .build();

        switch(streamDestination) {
            case request:
                channels.streamRequest().send(streamMessage);
                break;
            case response:
                channels.streamResponse().send(streamMessage);
                break;
            default:
                // code block
        }
    }

}
