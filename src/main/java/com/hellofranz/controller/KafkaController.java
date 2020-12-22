package com.hellofranz.controller;

import com.hellofranz.configuration.ProducerConfiguration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@Controller
public class KafkaController {

    private KafkaTemplate<String, String> template;

    public KafkaController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @GetMapping("/send")
    public String produceGet(@RequestParam String message,
                           @RequestParam(value = "to", required = false) String recipient, Model model) throws ExecutionException, InterruptedException {
        produce(message, recipient);
        model.addAttribute("message", message);
        return "sendSuccess";
    }

    /**
     *
     * @param message
     * @param recipient
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/send")
    @ResponseBody // i.e. return directly as HTML, not a view
    public void produce(@RequestParam String message,
                        @RequestParam(value = "to", required = false) String recipient) throws ExecutionException, InterruptedException {


        DateTime dt = new DateTime(DateTimeZone.UTC);

        if (recipient != null) {
            template.send(recipient, dt + " -- " + message);
        } else {
            ArrayList<String> topics = ProducerConfiguration.getAllTopics();
            for (String topic : topics) {
                System.out.println("Sending to " + topic);
                template.send(topic, dt + " -- " + message);
            }
        }
    }
}
