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

// we use @Controller instead of @RestController because we provide REST as well as standard HTML responses
@Controller
public class ProducerController {

    // our producer
    private KafkaTemplate<String, String> template;

    public ProducerController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    // vanilla API wrapper for requests that don't come through index.html
    @GetMapping("/send")
    public String produceGet(@RequestParam String message,
                           @RequestParam(value = "to", required = false) String recipient, Model model) throws ExecutionException, InterruptedException {
        produce(message, recipient);

        // returns an HTML 'template' with the message written to it
        model.addAttribute("message", message);
        return "sendSuccess";
    }

    @PostMapping("/send")
    @ResponseBody // i.e. return directly as HTML, not a view
    public void produce(@RequestParam String message,
                        @RequestParam(value = "to", required = false) String recipient) throws ExecutionException, InterruptedException {

        // add timestamp to message
        DateTime dt = new DateTime(DateTimeZone.UTC);

        // add new "user" if not exists
        if (recipient != null) {
            NativeAdmin.createTopicIfNotExists(recipient);
            template.send(recipient, dt + " -- " + message);
        } else {
            // otherwise send to everyone
            ArrayList<String> topics = NativeAdmin.getAllTopics();
            for (String topic : topics) {
                System.out.println("Sending to " + topic);
                template.send(topic, dt + " -- " + message);
            }
        }
    }
}
