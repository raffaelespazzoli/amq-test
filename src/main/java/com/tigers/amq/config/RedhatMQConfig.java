package com.tigers.amq.config;

import org.amqphub.spring.boot.jms.autoconfigure.AMQP10JMSAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
@Import({AMQP10JMSAutoConfiguration.class})
@Profile("redhatmq")
public class RedhatMQConfig {

}