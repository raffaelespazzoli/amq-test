package com.tigers.amq.stream;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@EnableBinding(EventChannels.class)
public class StreamConsumer {
    private static Logger log = LoggerFactory.getLogger(StreamConsumer.class);

    @Autowired
    private EventChannels channels;

    @StreamListener(target=EventChannels.STREAM_REQUEST, condition = "@checkHeaders.test(headers['event-type'])")
    public void handleRequestMessageEvent(String jsonMessage) {
        log.info("Handle stream message " + jsonMessage);
        Message<?> streamMessage = generateMessage(jsonMessage);
        log.info("Sending message [" + jsonMessage + "] to - " + EventChannels.STREAM_RESPONSE);
        channels.streamResponse().send(streamMessage);
    }

    @Transformer(inputChannel=EventChannels.STREAM_REQUEST, outputChannel=EventChannels.STREAM_RESPONSE)
    public Message<?> transformRequestMessageEvent(String jsonMessage) {
        log.info("Transform stream message " + jsonMessage);
        Message<?> streamMessage = generateMessage(jsonMessage);
        log.info("Sending message [" + jsonMessage + "] to - " + EventChannels.STREAM_RESPONSE);
        return streamMessage;
    }

    protected Message<?> generateMessage(String jsonMessage) {
        Map<String, String> map = new Gson().fromJson(jsonMessage, Map.class);

        Map<String, String> headers = new HashMap();
        headers.put("date", "2000-01-01");
        headers.put("domain", "maas");
        headers.put("type", "instance");
        String headerJson = new Gson().toJson(headers);

        Map<String, String> fields = new HashMap();
        fields.put("data", map.get("name"));
        String data = new Gson().toJson(fields);

        Message<?> streamMessage = MessageBuilder.withPayload(data.getBytes())
                .setHeader("header", headerJson)
                .setHeader("tokenId", "token")
                .build();

        return streamMessage;
    }


    @Bean
    public Predicate<String> checkHeaders() {
        return header -> {
            List<String> headerTokens = Arrays.stream(header.split("\\.")).filter(Objects::nonNull).collect(Collectors.toList());
            return headerTokens.size() == 4 && headerTokens.get(1).equals("key1") && headerTokens.get(2).equals("key2");
        };
    }

}
