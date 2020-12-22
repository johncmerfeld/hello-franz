package com.hellofranz.controller;

import com.hellofranz.nativekafka.NativeAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@Controller
public class ProducerController {

    private KafkaTemplate<String, String> template;

    public ProducerController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @GetMapping("/send")
    public String produceGet(@RequestParam String message,
                           @RequestParam(value = "to", required = false) String recipient, Model model) throws ExecutionException, InterruptedException {
        produce(message, recipient);

        // returns an HTML 'template' with the message written to it
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
            NativeAdmin.createTopicIfNotExists(recipient);
            template.send(recipient, dt + " -- " + message);
        } else {
            ArrayList<String> topics = NativeAdmin.getAllTopics();
            for (String topic : topics) {
                System.out.println("Sending to " + topic);
                template.send(topic, dt + " -- " + message);
            }
        }
    }
}
