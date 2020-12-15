# hello-franz
Hello World application built with Spring Boot and Apache Kafka

## Prerequisites beyond the scope of these instructions
  - A JDK (this project uses [Java 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) -- if you download a newer version, you will need to modify `init-spring.sh`)

## To run
  1. **Clone this repo**
```shell
git clone git@github.com:johncmerfeld/hello-franz.git
```

  2. **Download Kafka**

From [this page](https://kafka.apache.org/downloads), download the latest Kafka binary (this project was built with `kafka_2.13-2.6.0.tgz`). Kafka requires Zookeeper to run, but that software is bundled with the Kafka download. Un-tar the contents of the download and move the folder's contents to this repo – it is not strictly necessary that you do this, but subsequent instructions will assume you did something like this:
```shell
# from the repo's root directory
mv ~/Downloads/kafka_2.13-2.6.0.tgz .
tar -xzvf kafka_2.13-2.6.0.tgz
mv kafka_2.13-2.6.0/* .
rm -rf kafka_2.13-2.6.0 kafka_2.13-2.6.0.tgz
```

  3. **Download Gradle**

If you're running on a Mac, 
```shell
brew install gradle
```
will do the trick. Otherwise, use your OS's preferred package manager

  4. **Run the Spring Initializer**

Run 
```shell 
sh init-spring.sh
```
This will download the gradle config files from the [Spring Initializer](https://start.spring.io/) that we need to build our application (mostly this boils down to dependency management)

  5. **Initialize Zookeepr**

In a new terminal window, start up your Zookeeper server like this:
```shell
sh bin/zookeeper-server-start.sh config/zookeeper.properties
```

  6. **Initialize a Kafka Broker**

In a new terminal window, start up the Kafka server - which will handle the distribution of messages from Producers to Consumers - like this: 
```shell
sh bin/kafka-server-start.sh config/server.properties
```

  7. **Create a topic**

Your producers need a Kafka topic to publish to, and your consumer needs something to subscribe to: 
```shell
sh bin/kafka-topics.sh --create --topic myTopic -zookeeper localhost:2181 --replication-factor 1 --partitions 1
```

  8. **Initialize a Kafka consumer**

In order to check whether your application is working, create a Kafka consumer in yet one more terminal window: 
```shell
sh bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic myTopic
```

  9. **Build and start the Spring application**
If you've done everything right to this point, you should be able to execute a `gradle build` to bundle all of your dependencies...... In sum, it should look like this:
```shell
gradle build
gradle bootRun
```

  10. **Test the application**

  Go to [http://localhost:8080/kafka/produce?message=Hello, world!](http://localhost:8080/kafka/produce?message=Hello, world!") to send the message "Hello, world!" through your Kafka application. Go to the terminal window where your console consumer is running and verify that "Hello, world!" has been printed to the screen


TODO
3. Try to recreate from instructions
4. Try to simplify package
5. Create custom consumer
6. Learn more about what's going on here (and write about it!)
