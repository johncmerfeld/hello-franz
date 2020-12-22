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


    public static ArrayList<String> getAllTopics() throws ExecutionException, InterruptedException {

        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        Set topics = adminClient.listTopics(listTopicsOptions).names().get();

        ArrayList<String> results = new ArrayList<String>();
        results.addAll(topics);
        results.remove(conf.getIgnoreTopic());
        return results;

    }

    public static void createTopicIfNotExists(String topic) {
        System.out.println("Checking topic " + topic);
        boolean topicExists = false;
        try {
            topicExists = adminClient.listTopics().names().get().stream().anyMatch(topicName -> topicName.equalsIgnoreCase(topic));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (! topicExists) {
            System.out.println("Creating topic " + topic);
            NewTopic newTopic = new NewTopic(topic, 1, (short)1); //new NewTopic(topicName, numPartitions, replicationFactor)

            final CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));

            // Since the call is Async, Lets wait for it to complete.
            try {
                createTopicsResult.values().get(topic).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //adminClient.close();
        }
    }


}
