package com.hellofranz.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AdminConfiguration {

    @Value("${kafka.broker}")
    private String KAFKA_BROKER;

    private static String IGNORE_TOPIC;

    @Value("${kafka.producers.ignoretopic}")
    private void setIgnoreTopic(String topic) {
        IGNORE_TOPIC = topic;
    }

    @Bean
    public Map<String, Object> getAdminConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        return props;
    }

    @Bean
    public String getIgnoreTopic() {
        return IGNORE_TOPIC;
    }

}
