package com.hellofranz.controller;

import com.hellofranz.configuration.ProducerConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


@RestController
public class KafkaController {

    private KafkaTemplate<String, String> template;

    public KafkaController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    /**
     * TODO topic list from file! Global vs. individual sends, defult to Global
     * Lookup isNone in query strings
     * TODO entrypoint by topic??? // topic as queryString param
     * Other TODO script that creates a topic and adds to some kind of file?
     * @param message
     */
    @PostMapping("/send")
    public void produce(@RequestParam String message,
                        @RequestParam(value = "to", required = false) String recipient) {


        DateTime dt = new DateTime(DateTimeZone.UTC);

        if (recipient != null) {
            template.send(recipient, dt + " -- " + message);
        } else {
            String[] topics = ProducerConfiguration.getAllTopics();
            for (int i = 0; i < topics.length; i++) {
                template.send(topics[i], dt + " -- " + message);
            }
        }
    }

}
