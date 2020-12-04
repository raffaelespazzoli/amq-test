package com.tigers.amq.queue;

import com.google.gson.Gson;
import com.tigers.amq.rest.QueueDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class QueueProducer {

    private static Logger log = LoggerFactory.getLogger(QueueProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(QueueDestination dest, final String message) {
        final String correlationID = "CorrelationID";
        Map<String, String> headers = new HashMap();
        headers.put("date", "2000-01-01");
        headers.put("domain", "maas");
        headers.put("type", "instance");
        final String headerJson = new Gson().toJson(headers);

        log.info("Sending message [" + message + "] to - " + dest.queueName);
        jmsTemplate.send(dest.queueName, session -> {
            TextMessage txtMsg = session.createTextMessage(message);
            txtMsg.setJMSCorrelationID(correlationID);
            txtMsg.setStringProperty("header", headerJson);
            return txtMsg;
        });
    }

}