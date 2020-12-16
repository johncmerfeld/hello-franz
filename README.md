# hello-franz
Hello World application built with Spring Boot and Apache Kafka. I wasn't having any luck with other online tutorials for getting this stuff to work together in a proof-of-concept example, so I decided to create my own. I can't promise that this will work on the first try, but I've tried to include the exact code structure and set of steps you need to see how Kafka can work behind a simple web app!

## Prerequisites beyond the scope of these instructions
  - A JDK (this project uses [Java 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) -- if you download a newer version, you will need to modify `init-spring.sh`)

## To run
  1. **Clone this repo**
```shell
git clone git@github.com:johncmerfeld/hello-franz.git
```

  2. **Download Kafka**

From [this page](https://kafka.apache.org/downloads), download the latest Kafka binary (this project was built with `kafka_2.13-2.6.0.tgz`). Kafka requires Zookeeper to run, but that software is bundled with the Kafka download. Un-tar the contents of the download and move the folder's contents to this repo – it is not strictly necessary that you do this, but subsequent instructions will assume you did something like:
```shell
# from the repo's root directory
mv ~/Downloads/kafka_2.13-2.6.0.tgz .
tar --exclude=LICENSE -xzvf kafka_2.13-2.6.0.tgz
mv kafka_2.13-2.6.0/* .
rm -rf kafka_2.13-2.6.0 kafka_2.13-2.6.0.tgz
```

  3. **Download Gradle**

If you're running on a Mac with Homebrew, 
```shell
brew install gradle
```
will do the trick. Otherwise, use your OS's preferred package manager.

  4. **Initialize Zookeeper**

In a new terminal window, start up your Zookeeper server like this:
```shell
# Terminal window 2
sh bin/zookeeper-server-start.sh config/zookeeper.properties
```

  5. **Initialize a Kafka Broker**

In a new terminal window, start up the Kafka server - which will handle the distribution of messages from Producers to Consumers - like this: 
```shell
# Terminal window 3
sh bin/kafka-server-start.sh config/server.properties
```

  6. **Create two topic**

Your producers need Kafka topics to publish to, and your consumer needs something to subscribe to: 
```shell
sh bin/kafka-topics.sh --create --topic johncmerfeld -zookeeper localhost:2181 --replication-factor 1 --partitions 1
sh bin/kafka-topics.sh --create --topic kjmerf -zookeeper localhost:2181 --replication-factor 1
```

This creates the topics `johncmerfeld` and `kjmerf` - we are essentially pretending that this is a messaging service, and that `johncmerfeld` and `kjmerf` are two of the users

  7. **Initialize a Kafka consumer**

In order to check whether your application is working, create a Kafka consumer in yet one more terminal window: 
```shell
# Terminal window 4
sh bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic johncmerfeld
# optional: Terminal window 5
sh bin/kafka-console-consumer.sh --boostrap-server localhost:9092 --topic kjmerf

```

  8. **Build and start the Spring application**
If you've done everything right to this point, you should be able to execute a `gradle build` to bundle all of your dependencies...... In sum, it should look like this:
```shell
gradle build
gradle bootRun
```

  9. **Test the application**

  Go to [http://localhost:8080/send?message=Hello, world!&to=johncmerfeld](http://localhost:8080/send?message=Hello,%20world!&to=johncmerfeld) to send the message "Hello, world!" through your Kafka application. Go to the terminal window where your `johncmerfeld` console consumer is running and verify that "Hello, world!" has been printed to the screen with a timestamp. If you go to the `kjmerf` consumer, you will see that no message is printed there. Try changing the `to` parameter in your URL to send a message to `kjmerf`!

