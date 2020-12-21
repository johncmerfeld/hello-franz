package com.hellofranz.controller;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.web.bind.annotation.*;

import com.hellofranz.nativekafka.NativeConsumer;
//import com.hybriscx.demo.kafka.kafkademo.KafkaAwareController;


@RestController
public class ConsumerController {

    /**
     * Consume message. This controller function polls the messages for a topic available in Kafka.
     *
     * @param username the topic
     * @return the string
     */
    @GetMapping("/receive")
    @ResponseBody
    public String consumeMessage(@RequestParam String username) {

        //ConsumerFactory<String, Object> consumerFactory = getConsumerFactoryInstance();

        Consumer<String, Object> consumer = NativeConsumer.createConsumer();

        /* FIXME this is probably bad logic */
        consumer.subscribe(Collections.singletonList(username));

        // poll messages from last 10 days
        ConsumerRecords<String, Object> consumerRecords = consumer.poll(Duration.ofDays(10));

        // print on console or send back as a string/json. Feel free to change controller function implementation for ResponseBody
        String results = "";
        consumerRecords.forEach(action -> {
            System.out.println(action.value());
        });
        final Iterator<ConsumerRecord<String, Object>> recordIterator = consumerRecords.iterator();
        while (recordIterator.hasNext()) {
            results += recordIterator.next().value() + "<br>";
        }

        return results;
    }
}