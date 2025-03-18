package com.axonivy.connector.keycloak.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.keycloak.www.client.AdminrealmsCredentials;
import org.keycloak.www.client.UserRepresentation;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import com.axonivy.connector.keycloak.constants.ProcessPaths;
import com.axonivy.connector.keycloak.service.UserServices;
import com.axonivy.connector.keycloak.test.utils.KeycloakTestUtils;
import com.axonivy.connector.keycloak.utils.UserUtils;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PortBinding;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class KeycloakTest {
  private static final DockerImageName KEYCLOAK_IMAGE = DockerImageName.parse("quay.io/keycloak/keycloak:26.1");
  private static final String FINISHED_SET_UP_LOG_REGEX = ".*Installed features:.*\\n";
  private static String password;
  private static String username;
  private static String realmName;
  private static String url;
  private static GenericContainer<?> keycloakContainer;
  private static UserServices userServices = new UserServices();

  @SuppressWarnings("resource")
  @BeforeAll
  static void setupDocker() throws IOException {
    username = System.getProperty("kcUsername");
    password = System.getProperty("kcPassword");
    realmName = System.getProperty("kcRealmName");
    url = System.getProperty("kcUrl");

    if (StringUtils.isEmpty(username)) {
      try (var in = KeycloakTest.class.getResourceAsStream("credentials.properties")) {
        if (in != null) {
          Properties props = new Properties();
          props.load(in);
          username = (String) props.get("username");
          password = (String) props.get("password");
        }
      }
    }

    Network network = Network.newNetwork();
    keycloakContainer = new GenericContainer<>(KEYCLOAK_IMAGE).withNetwork(network)
        .withNetworkAliases("octopus_keycloak").withExposedPorts(8090)
        .withCreateContainerCmdModifier(
            cmd -> cmd.withHostConfig(HostConfig.newHostConfig().withNetworkMode(network.getId())
                .withPortBindings(new PortBinding(Ports.Binding.bindPort(8080), new ExposedPort(8090)))))
        .withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", username).withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", password)
        .withEnv("KC_HOSTNAME", url)
        .withCopyFileToContainer(MountableFile.forHostPath("../keycloak-connector-demo/docker/ivytestrealm.json"),
            "/opt/keycloak/data/import/ivytestrealm.json")
        .withCommand("start-dev --import-realm").waitingFor(Wait.forLogMessage(FINISHED_SET_UP_LOG_REGEX, 1));
    keycloakContainer.start();
  }
  
   @Test
  void test_createNewUser(BpmClient client) throws NoSuchFieldException {
    ExecutionResult executionResult = KeycloakTestUtils
        .getSubProcessWithNameAndPath(client, ProcessPaths.USER_SUB_PROCESSES, ProcessPaths.CREATE_USER_START_NAME)
        .execute(realmName, createMockCreateUserRequest());
    var userId = executionResult.data().last().get(ProcessPaths.USER_ID);
    assertTrue(userId instanceof String);
    assertTrue(StringUtils.isNotBlank((String) userId));
  }

  @AfterAll
  static void clearTestContainer() {
    keycloakContainer.close();
  }
  
  private UserRepresentation createMockCreateUserRequest() {
    UserRepresentation userRequest = new UserRepresentation();
    List<AdminrealmsCredentials> credentials = List.of(UserUtils.createTemporaryPassword());
    userRequest.setCredentials(credentials);
    userRequest.setUsername("Dino");
    userRequest.setFirstName("Dino");
    userRequest.setLastName("Palmer");
    userRequest.setEmail("dino@mail.com");
    userRequest.setEnabled(true);
    return userRequest;
  }

}
