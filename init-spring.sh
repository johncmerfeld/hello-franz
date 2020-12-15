curl https://start.spring.io/starter.zip -d language=java \
 -d bootVersion=2.2.1.RELEASE \
 -d dependencies=web,kafka \
 -d packageName=com.okta.javakafka \
 -d name=kafka-java \
 -d type=gradle-project \
 -d javaVersion=8 \
 -o kafka-java.zip
