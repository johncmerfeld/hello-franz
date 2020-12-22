package com.hellofranz.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;


@Configuration
public class ConsumerConfiguration implements InitializingBean {
    @Value("${kafka.broker}")
    private String KAFKA_BROKER;

    @Value("${kafka.deserializers.str}")
    private Object STRING_DESERIALIZER;

    @Value("${kafka.deserializers.long}")
    private Object LONG_DESERIALIZER;

    @Value("${kafka.consumers.group_id}")
    private String GROUP_ID;

    @Value("${kafka.consumers.trusted_packages}")
    private String TRUSTED_PACKAGES;

    @Value("${kafka.consumers.auto_offset_reset}")
    private String AUTO_OFFSET_RESET;

    @Bean
    public Map<String, Object> getConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        System.out.println(KAFKA_BROKER);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LONG_DESERIALIZER);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, STRING_DESERIALIZER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET);
        return props;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Bootstrap server:" + KAFKA_BROKER);
    }

}
