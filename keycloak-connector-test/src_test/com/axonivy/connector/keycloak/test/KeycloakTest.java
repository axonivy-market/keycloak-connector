package com.axonivy.connector.keycloak.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.axonivy.connector.keycloak.constants.RequestConstants;
import com.axonivy.connector.keycloak.enums.ConfigVariables;
import com.axonivy.connector.keycloak.test.utils.KeycloakTestUtils;
import com.axonivy.connector.keycloak.utils.UserUtils;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PortBinding;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;

@IvyProcessTest
public class KeycloakTest {
  private static final DockerImageName KEYCLOAK_IMAGE = DockerImageName.parse("quay.io/keycloak/keycloak:26.1");
  private static final String FINISHED_SET_UP_LOG_REGEX = ".*Profile dev activated.*";
  private static String password;
  private static String username;
  private static String realmName;
  private static String url;
  private static GenericContainer<?> keycloakContainer;
  private static Network network;

  @BeforeAll
  static void setupDocker(AppFixture appFixture) throws IOException {
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
          realmName = (String) props.get("realmName");
          url = (String) props.get("url");
        }
      }
    }
    updateTestFixture(appFixture);
    startKeycloakDockerContainer();
  }

  private static void updateTestFixture(AppFixture appFixture) {
    appFixture.var(ConfigVariables.USERNAME.getValue(), username);
    appFixture.var(ConfigVariables.PASSWORD.getValue(), password);
    appFixture.var(ConfigVariables.REALM_NAME.getValue(), realmName);
    appFixture.var(ConfigVariables.URL.getValue(), url);
  }

  @AfterAll
  static void clearTestContainer() {
    keycloakContainer.close();
    network.close();
  }

  @Test
  void test_createAndDeleteNewUser(BpmClient client) throws NoSuchFieldException {
    assertEquals(1, countUsers(client));
    ExecutionResult executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client, RequestConstants.USER_SUB_PROCESSES,
        RequestConstants.CREATE_USER_PROCESS_NAME).execute(realmName, createMockCreateUserRequest());
    String userId = String.class.cast(executionResult.data().last().get(RequestConstants.USER_ID));
    assertTrue(userId instanceof String);
    assertTrue(StringUtils.isNotBlank(userId));
    assertEquals(2, countUsers(client));
    executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client, RequestConstants.USER_SUB_PROCESSES,
        RequestConstants.DELETE_USER_PROCESS_NAME).execute(realmName, userId);
    assertEquals(1, countUsers(client));
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
  
  @SuppressWarnings("unchecked")
  private int countUsers(BpmClient client) throws NoSuchFieldException {
    ExecutionResult executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client,
        RequestConstants.USER_SUB_PROCESSES, RequestConstants.GET_USERS_PROCESS_NAME).execute(realmName, null);
    List<UserRepresentation> users = (List<UserRepresentation>) executionResult.data().last()
        .get(RequestConstants.USERS);
    return users.size();
  }

  @SuppressWarnings("resource")
  private static void startKeycloakDockerContainer() {
    network = Network.newNetwork();
    keycloakContainer = new GenericContainer<>(KEYCLOAK_IMAGE).withNetwork(network)
        .withNetworkAliases("octopus_keycloak").withExposedPorts(8080)
        .withCreateContainerCmdModifier(
            cmd -> cmd.withHostConfig(HostConfig.newHostConfig().withNetworkMode(network.getId())
                .withPortBindings(new PortBinding(Ports.Binding.bindPort(8090), new ExposedPort(8080)))))
        .withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", username).withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", password)
        .withEnv("KC_HOSTNAME", url)
        .withCommand("start-dev").waitingFor(Wait.forLogMessage(FINISHED_SET_UP_LOG_REGEX, 1));
    keycloakContainer.start();
  }
}
