package com.hellofranz.controller;

import com.hellofranz.configuration.ProducerConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@RestController
public class KafkaController {

    private KafkaTemplate<String, String> template;

    public KafkaController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    /**
     * TODO topic list from file! Global vs. individual sends, defult to Global
     * @param message
     */
    @PostMapping("/send")
    public void produce(@RequestParam String message,
                        @RequestParam(value = "to", required = false) String recipient) throws ExecutionException, InterruptedException {


        DateTime dt = new DateTime(DateTimeZone.UTC);

        if (recipient != null) {
            template.send(recipient, dt + " -- " + message);
        } else {
            ArrayList<String> topics = ProducerConfiguration.getAllTopics();
            for (String topic : topics) {
                template.send(topic, dt + " -- " + message);
            }
        }
    }

}
