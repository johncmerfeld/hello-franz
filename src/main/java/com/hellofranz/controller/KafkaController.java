package com.hellofranz.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@RestController
public class KafkaController {

    private KafkaTemplate<String, String> template;

    public KafkaController(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @GetMapping("/kafka/produce")
    public void produce(@RequestParam String message) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        template.send("myTopic", strDate + " -- " + message);
    }

}
