# Introduction

This project exposes a simple REST endpoint where the service greeting is available at this address http://hostname:port/greeting and returns a json Greeting message

{
    "content": "Hello, World!",
    "id": 1
}

The id of the message is incremented for each request. To customize the message, you can pass as parameter the name of the person that you want to send your greeting.

# Build

To build the project, open a terminal and execute this apache maven command

```
mvn clean package
```

# Launch and Test

To start the Eclipse Vert.x application, execute this Java command within a terminal

```
java -jar target/vertx-rest-1.0-SNAPSHOT-fat.jar
```

and execute some HTTP Get requests to get a response from the Rest endpoint.

```
http http://localhost:8080/greeting
http http://localhost:8080/greeting name==Charles
curl http://localhost:8080/greeting
curl http://localhost:8080/greeting -d name=Bruno
```

# OpenShift

TODO

