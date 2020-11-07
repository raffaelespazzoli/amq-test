package com.tigers.amq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import ch.qos.logback.core.net.ssl.SSL;

@Configuration
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    String BROKER_URL;

    @Value("${spring.activemq.user}")
    String BROKER_USERNAME;

    @Value("${spring.activemq.password}")
    String BROKER_PASSWORD;

    @Value("${spring.activemq.use_tls}")
    boolean use_tls;

    @Value("${spring.activemq.truststore}")
    String truststore;

    @Value("${spring.activemq.truststorePassword}")
    String truststorePassword;    

    @Bean
    public ActiveMQConnectionFactory connectionFactory() throws Exception{
        ActiveMQConnectionFactory connectionFactory=null;
        if (use_tls) {
            ActiveMQSslConnectionFactory sSLConnectionFactory = new ActiveMQSslConnectionFactory();
            sSLConnectionFactory.setTrustStore(truststore);
            sSLConnectionFactory.setTrustStorePassword(truststorePassword);
            connectionFactory=sSLConnectionFactory;
        } else {
            connectionFactory = new ActiveMQConnectionFactory();
        }
        
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUserName(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() throws Exception {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() throws Exception {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-1");
        return factory;
    }

}