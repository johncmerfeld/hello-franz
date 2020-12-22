package com.hellofranz.nativekafka;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.Consumer;

import com.hellofranz.configuration.ConsumerConfiguration;

@Component
public class NativeConsumer {

    private static ConsumerConfiguration conf;

    @Autowired
    public void setConf(ConsumerConfiguration conf) {
        this.conf = conf;
    }

    public static Consumer<String, Object> createConsumer(String topic) {
        final Map<String, Object> props = conf.getConsumerConfig();

        NativeAdmin.createTopicIfNotExists(topic);

        // Create the consumer using props.
        final Consumer<String, Object> consumer = new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }

    /*
    static void runConsumer() throws InterruptedException {
        final Consumer<String, Object> consumer = createConsumer();

        final int giveUp = 100;   int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<String, Object> consumerRecords =
                    consumer.poll(1000);

            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }

     */
}