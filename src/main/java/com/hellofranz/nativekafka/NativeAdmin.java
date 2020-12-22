package com.hellofranz.nativekafka;

import com.hellofranz.configuration.AdminConfiguration;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Kafka AdminClient class to handle direct manipulation of the Kafka server
 */
@Component
public class NativeAdmin {

    private static AdminConfiguration conf;
    private static AdminClient adminClient;

    @Autowired
    public void setConf(AdminConfiguration conf) {
        this.conf = conf;
        final Map<String, Object> props = conf.getAdminConfig();
        this.adminClient = AdminClient.create(props);
    }


    public static ArrayList<String> getAllTopics() {
        ArrayList<String> results = new ArrayList<String>();
        try {
            ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
            listTopicsOptions.listInternal(true);
            Set topics = adminClient.listTopics(listTopicsOptions).names().get();
            results.addAll(topics);
            results.remove(conf.getIgnoreTopic());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void createTopicIfNotExists(String topic) {
        boolean topicExists = false;
        try {
            topicExists = adminClient.listTopics().names().get().stream().anyMatch(topicName ->
                    topicName.equalsIgnoreCase(topic));

            if (! topicExists) {
                NewTopic newTopic = new NewTopic(topic, 1, (short) 1);

                final CreateTopicsResult createTopicsResult =
                        adminClient.createTopics(Collections.singleton(newTopic));

                // Since the call is Async, let's wait for it to complete.
                createTopicsResult.values().get(topic).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
