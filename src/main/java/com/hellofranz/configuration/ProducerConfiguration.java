package com.hellofranz.configuration;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Configuration
public class ProducerConfiguration {

    private static Map<String, Object> config;

    @Value("${kafka.broker}")
    private String KAFKA_BROKER;

    @Value("${kafka.serializers.str}")
    private Object STRING_SERIALIZER;

    private static String IGNORE_TOPIC;

    @Value("${kafka.producers.ignoretopic}")
    private void setIgnoreTopic(String topic) {
        IGNORE_TOPIC = topic;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public Map<String, Object> producerConfigurations() {
        Map<String, Object> props = new HashMap<>();
        System.out.println(KAFKA_BROKER);

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER);

        config = props;

        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public static ArrayList<String> getAllTopics() throws ExecutionException, InterruptedException {
        AdminClient adminClient = AdminClient.create(config);
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        Set topics = adminClient.listTopics(listTopicsOptions).names().get();

        /* TODO document this future stuff */
        /* TODO test this */
        ArrayList<String> results = new ArrayList<String>();
        results.addAll(topics);
        results.remove(IGNORE_TOPIC);
        System.out.println("Remoevd " + IGNORE_TOPIC);
        return results;

    }


}
