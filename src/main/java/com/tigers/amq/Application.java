package com.tigers.amq;

import org.amqphub.spring.boot.jms.autoconfigure.AMQP10JMSAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;

@SpringBootApplication(exclude={AMQP10JMSAutoConfiguration.class, ActiveMQAutoConfiguration.class, RabbitAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
