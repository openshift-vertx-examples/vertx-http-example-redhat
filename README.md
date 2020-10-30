https://appdev.openshift.io/docs/vertx-runtime.html#mission-rest-http-vertx

## How to Build
You can build this project with this maven command:
```
mvn clean install
```

## Deploying to [Red Hat OpenShift](https://www.openshift.com/)
This project relies in [Eclipse JKube](https://github.com/eclipse/jkube)'s [OpenShift Maven Plugin](https://www.eclipse.org/jkube/docs/openshift-maven-plugin) for deploying to Red Hat OpenShift. OpenShift Maven Plugin is available in openshift profile. You can deploy this application using this command:
```
mvn oc:deploy -Popenshift
```

Once you're done with testing, you can undeploy this using this goal:
```
mvn oc:undeploy -Popenshift
```
