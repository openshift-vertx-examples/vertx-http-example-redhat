# Introduction

This project exposes a simple REST endpoint where the service greeting is available at this address http://hostname:port/greeting and returns a json Greeting message

```json
{
    "content": "Hello, World!",
    "id": 1
}
```

The id of the message is incremented for each request. To customize the message, you can pass as parameter the name of the person that you want to send your greeting.
You can perform this task in three different ways:

1. Build and launch using vert.x or the vert.x maven plug-in.
1. Build and deploy using OpenShift.
1. Build, deploy, and authenticate using OpenShift Online.

The id of the message is incremented for each request. To customize the message, you can pass as parameter the name of the person that you want to send your greeting.

# Pre-Requisites

# Build the Project

1. Execute the following apache maven command:

```bash
mvn clean package
```

# Launch and Test

1. Execute the following apache maven command:

```
java -jar target/vertx-rest-1.0.0-SNAPSHOT.jar
```

1. Execute the following HTTP Get requests to get a response from the Rest endpoint:

```
http http://localhost:8080/greeting
http http://localhost:8080/greeting name==Charles
curl http://localhost:8080/greeting
curl http://localhost:8080/greeting?name=Bruno
```

# Launch using Vert.x maven plugin

1. Execute the following command:

```bash
mvn clean vertx:run
```

# Build and Deploy Using OpenShift

1. Deploy the application.

- To build, deploy & launch the pod running the Eclipse Vert.x application

```
mvn clean fabric8:deploy -Popenshift
```
1. Access the service using curl or http tool.

```
http $(minishift service vertx-rest --url=true)/greeting
curl $(minishift service vertx-rest --url=true)/greeting
```

# OpenShift Online

1. Go to [OpenShift Online](https://console.dev-preview-int.openshift.com/console/command-line) to get the token used by the oc client for authentication and project access.
1. On the oc client, execute the following command to replace MYTOKEN with the one from the Web Console:
    ```
    oc login https://api.dev-preview-int.openshift.com --token=MYTOKEN
    ```

1. Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine and start the pod.

    ```
    mvn clean package fabric8:deploy -Popenshift
    ```
1. Use the Host/Port address exposed by the route to access the REST endpoint
    ```
    oc get route/vertx-rest
    NAME         HOST/PORT                                                    PATH      SERVICE           TERMINATION   LABELS
    vertx-rest   <HOST_PORT_ADDRESS>             vertx-rest:8080                 expose=true,group=org.jboss.obsidian.quickstart,project=vertx-rest,provider=fabric8,version=1.0-SNAPSHOT
    ```
1. Call the endpoint using curl or httpie tool
    ```
    http http://<HOST_PORT_ADDRESS>/greeting
    http http://<HOST_PORT_ADDRESS>/greeting name==Bruno
    
    or 
    
    curl http://<HOST_PORT_ADDRESS>/greeting
    curl http://<HOST_PORT_ADDRESS>/greeting name==Bruno
    ```


