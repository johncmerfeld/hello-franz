#!/usr/bin/env sh

# download build automation dependencies from Spring Initializer
curl https://start.spring.io/starter.zip -d language=java \
 -d bootVersion=2.2.1.RELEASE \
 -d dependencies=web,kafka \
 -d packageName=com.hellofranz \
 -d name=hello-franz \
 -d type=gradle-project \
 -d javaVersion=8 \
 -o kafka-java.zip

# unzip without overwriting Java files
unzip -o kafka-java.zip -x "src/**" ".gitignore"
