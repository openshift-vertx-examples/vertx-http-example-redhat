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
java -jar target/vertx-rest-1.0-SNAPSHOT-fat.jar run org.jboss.obsidian.quickstart.RestApplication
```

and execute some HTTP Get requests to get a response from the Rest endpoint.

```
http http://localhost:8080/greeting
http http://localhost:8080/greeting name==Charles
curl http://localhost:8080/greeting
curl http://localhost:8080/greeting?name=Bruno
```

# Launch using Vert.x maven plugin

```
mvn clean package vertx:run
```

# OpenShift

- To build & deploy

```
mvn clean package fabric8:deploy -Popenshift
```
- And to run/launch the pod

```
mvn fabric8:start -Popenshift
```

- Access the service using curl or httpie tool

```
http $(minishift service vertx-rest --url=true)/greeting
curl $(minishift service vertx-rest --url=true)/greeting
```

# OpenShift Online

- Connect to the OpenShift Online machine (e.g. https://console.dev-preview-int.openshift.com/console/command-line) to get the token to be used with the oc client to be authenticated and access the project
- Open a terminal and execute this command using the oc client where you will replace the MYTOKEN With the one that you can get from the Web Console
```
oc login https://api.dev-preview-int.openshift.com --token=MYTOKEN
```
- Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine
```
mvn clean package fabric8:deploy -Popenshift
```
- And to run/launch the pod
```
mvn fabric8:start -Popenshift
```
- Create the route to access the service 
```
oc expose service/vertx-rest --port=8080 
```
- Get the route url
```
oc get route/vertx-rest
NAME         HOST/PORT                                                    PATH      SERVICE           TERMINATION   LABELS
vertx-rest   vertx-rest-obsidian.1ec1.dev-preview-int.openshiftapps.com             vertx-rest:8080                 expose=true,group=org.jboss.obsidian.quickstart,project=vertx-rest,provider=fabric8,version=1.0-SNAPSHOT
```
- Use the Host/Port address to access the REST endpoint
```
http http://vertx-rest-obsidian.1ec1.dev-preview-int.openshiftapps.com/greeting
http http://vertx-rest-obsidian.1ec1.dev-preview-int.openshiftapps.com/greeting?name=Charles
```


