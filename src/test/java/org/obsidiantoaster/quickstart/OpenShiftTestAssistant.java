package org.obsidiantoaster.quickstart;

import com.jayway.restassured.RestAssured;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceVisitFromServerGetDeleteRecreateApplicable;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.OpenShiftClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class OpenShiftTestAssistant {

    private final OpenShiftClient client;
    private final String project;
    private String applicationName;
    private NamespaceVisitFromServerGetDeleteRecreateApplicable<List<HasMetadata>, Boolean> application;

    public OpenShiftTestAssistant() {
        client = new DefaultKubernetesClient().adapt(OpenShiftClient.class);
        project = client.getNamespace();
    }

    public String deployApplication() throws IOException {
        applicationName = System.getProperty("app.name");
        File list = new File("target/classes/META-INF/fabric8/openshift.yml");
        try (FileInputStream fis = new FileInputStream(list)) {
            application = client.adapt(OpenShiftClient.class).load(fis);
            List<HasMetadata> entities = application.createOrReplace();
            
            Optional<String> first = entities.stream()
                .filter(hm -> hm instanceof DeploymentConfig)
                .map(hm -> (DeploymentConfig) hm)
                .map(dc -> dc.getMetadata().getName()).findFirst();
            if (applicationName == null && first.isPresent()) {
                applicationName = first.get();
            }

            System.out.println("Application deployed, " + entities.size() + " object(s) created.");

            Route route = client.adapt(OpenShiftClient.class).routes()
                .inNamespace(project).withName(applicationName).get();
            assertThat(route).isNotNull();
            RestAssured.baseURI = "http://" + Objects.requireNonNull(route).getSpec().getHost();
            System.out.println("Route url: " + RestAssured.baseURI);
        }

        return applicationName;
    }

    public void cleanup() {
        if (application != null) {
            application.delete();
        }
    }

    public void awaitApplicationReadinessOrFail() {
        await().atMost(5, TimeUnit.MINUTES).until(() -> {
                List<Pod> list = client.pods().inNamespace(project).list().getItems();
                return list.stream().filter(pod ->
                    pod.getMetadata().getName().startsWith(applicationName))
                    .filter(this::isRunning)
                    .collect(Collectors.toList()).size() >= 1;
            }
        );

    }

    private boolean isRunning(Pod pod) {
        return "running".equalsIgnoreCase(pod.getStatus().getPhase());
    }

}
