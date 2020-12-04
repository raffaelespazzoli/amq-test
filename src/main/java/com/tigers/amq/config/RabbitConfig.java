package com.tigers.amq.config;


import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import({RabbitAutoConfiguration.class})
@Profile("rabbit")
public class RabbitConfig {
}
