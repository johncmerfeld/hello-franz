# hello-franz
This is a "Hello World+" application built with Spring Boot and Apache Kafka. I wasn't having any luck with other online tutorials for getting these technologies to work together in a proof-of-concept example, so I decided to create my own. I can't promise that this will work on the first try for everyone, but I've tried to include the exact code structure and set of steps you need to see how Kafka can work behind a simple web app! I call it "Hello World+" because there is some actual (albeit minor) functionality implemented too.

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

## Project structure

### Non-Java files

The most important file at the root level is build.gradle. It contains information about all of the 3rd-party Java libraries we need. When you execute `gradle build`, all of the listed dependencies are downloaded from Maven (although we could specify a different source) and added to the `libs/` directory, allowing us to `import` them in our Java files. You'll notice that build.gradle basically references 3 technologies: Kafka, Spring Boot, and ThymeLeaf.

Inside the `src/` directory, the files we need at runtime are stored in `main/`, while those we need for testing are stored in `test/`

Inside `src/main/`, we have separate directories for our Java files (`java/`) and everything else (`resources/`). Inside `resources/`, we see some runtime configuration files as well as the `static/` and `templates/` directories, which contain resources used by the Web frontend. Most of the files in `resources` are in the default location expected by Spring and ThymeLeaf. 

`src/main/resources/application.yml` is used by Spring to "inject" values into our runtime code without the need to write any kind of file reader. Many formats are valid for this file, but I chose YAML since it is commonly used by other tools. Right now, `application.yml` is pretty simple, but it may grow in detail as the scope of hello-franz grows.

Inside `src/main/resources/static/` are HTML and JavaScript files that allow the user to interact with the main service. The files in `templates/` are similar, except that they are written in a dialect of XML that is compatible with ThymeLeaf, which allows us to easily write content directly from the Java backend to the Web frontend. 

### Java files

Following Java convention, the folder structure inside `src/main/java` simulates the different parts of an internet domain read in reverse (i.e., hellofranz.com). Inside the `hellofranz` package root is a `main`-like file required by Spring. It contains the `HelloFranzApplication` class, whose `main` method is what actually gets run when we execute `gradle bootRun`.

Inside the `configuration/` directory are config classes that use Spring to load in properties of our Kafka producer, consumer, and administrator. The `nativekafka/` directory contains classes that uses configurations to create instances of the consumer and admin. We don't need to do this for the Kafka producer because it is written entirely in Spring's Kafka API, instead of the native Kafka Java library. The consumer and admin do use the Java library, however, so we need to define them in greater detail. This was done as a design choice - the application would behave slightly different if we were forced to use Spring Kafka's default behavior.

Finally, the `controller/` directory contains the code that actually responds to the user by defining methods that respond to various HTTP requests. The controller layer is the most "forward-facing" part of the backend, built on top of the classes we mentioned above. For example, the consumer controller creates a `nativekafka.NativeConsumer`, and instantiates it using information from `configuration.ConsumerConfiguration`.

## Chronological view from the user's perspective

Let's consider what happened in our example above. When you click `Send`, the HTTP POST request is issued to the `/send` endpoint (see `kafkaforms.js` for how this data gets sent). POST requests to the `/send` endpoint are handled by the `ProducerController`, which keeps a Kafka producer template running in the background. When a new message comes in, the controller thinks of the message's intended recipient as a Kafka topic. It asks the Kafka administrator to check whether the topic exists, and if not, to create it. It then publishes the message to that topic. If the POST request has no recipient attached, the controller asks the administrator for a list of all of the existing topics, and publishes the message to all of them.

When we "check" our messages as a particular user, a GET request is issued to the `/receive` endpoint, which is handled by the `ConsumerController`. It creates a Kafka `NativeConsumer` and polls the server for all messages that have come in for that particular topic since the last request was made (if the topic is not recognized, the administrator will create one, but it won't have any messages). These messages are returned to the `index.html` page, which uses JQuery (JavaScript) to print them all as a list on the screen.

## Chronological view from the application's perspective

My understanding of Spring is far from perfect, so this section is in progress. I think a key insight for me was the difference between using the `new` keyword to create instances of objects, and instead using `@Autowired` to load singleton instances of classes within other classes. Notice, for example, how the `NativeConsumer` loads in its `ConsumerConfiguration` without using `new`. This is possible because we define the `ConsumerConfiguration` as a `@Configuration` and its method `getConsumerConfig()` as a `@Bean`. This appears to be how Spring knows to make the method "publicly" available and ready to be "wired" into `NativeConsumer`. I think the best way to learn more about Spring's particular brand of inversion of control is to get to know this codebase and see what happens when you remove the various annotations. 