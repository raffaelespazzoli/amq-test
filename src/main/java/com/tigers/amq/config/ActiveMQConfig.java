package com.tigers.amq.config;

import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@Configuration
@Import({ActiveMQAutoConfiguration.class})
@Profile("activemq")
public class ActiveMQConfig {

}