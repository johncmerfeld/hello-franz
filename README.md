# hello-franz
This is a Hello World+ application built with Spring Boot and Apache Kafka. I wasn't having any luck with other online tutorials for getting this stuff to work together in a proof-of-concept example, so I decided to create my own. I can't promise that this will work on the first try, but I've tried to include the exact code structure and set of steps you need to see how Kafka can work behind a simple web app!

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
cp -r ~/Downloads/kafka_2.13-2.6.0.tgz .
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

  8. **Build and start the Spring application**
If you've done everything right to this point, you should be able to execute a `gradle build` to bundle all of your dependencies...... In sum, it should look like this:
```shell
gradle build
gradle bootRun
```

  9. **Test the application**

  To really get a feel for what we're doing here, open [http://localhost:8080](http://localhost:8080/) in **two** separate tabs. In the first one, type in a random message in the `Message` field and any made-up "user name" in the `To` field, and cilck `Send`. You should see the word `Sent!` flash on your screen. You can send as many as you like.

  Now go to the second tab. In the `Username` field below `Check your unread messages`, type in the same name you sent the message to above. Click `Get latest messages`, and the message(s) you sent before should be printed to your screen! If you have multiple devices on the same network, you can test the send/receive functionality between them.

# How this works

A full explanation of [Kafka](https://kafka.apache.org/intro) or [Spring](https://docs.spring.io/spring-framework/docs/current/reference/html/overview.html) is outside the scope of this project, but I will try to talk through how this application works so that others can get things working. Additionally, the comments in the Java files are meant to be instructive (to me and whoever reads this).