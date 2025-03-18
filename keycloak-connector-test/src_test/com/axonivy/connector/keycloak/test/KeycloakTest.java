package com.axonivy.connector.keycloak.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.keycloak.www.client.AdminrealmsCredentials;
import org.keycloak.www.client.UserRepresentation;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import com.axonivy.connector.keycloak.bo.UserQuery;
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

  @SuppressWarnings("resource")
  @BeforeAll
  static void setupDocker(AppFixture appFixture) throws IOException {
    username = System.getProperty("kcUsername");
    password = System.getProperty("kcPassword");
    realmName = System.getProperty("kcRealmName");
    url = System.getProperty("kcUrl");

    appFixture.var(ConfigVariables.USERNAME.getValue(), username);
    appFixture.var(ConfigVariables.PASSWORD.getValue(), password);
    appFixture.var(ConfigVariables.REALM_NAME.getValue(), realmName);
    appFixture.var(ConfigVariables.URL.getValue(), url);

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

    network = Network.newNetwork();
    keycloakContainer = new GenericContainer<>(KEYCLOAK_IMAGE).withNetwork(network)
        .withNetworkAliases("octopus_keycloak").withExposedPorts(8080)
        .withCreateContainerCmdModifier(
            cmd -> cmd.withHostConfig(HostConfig.newHostConfig().withNetworkMode(network.getId())
                .withPortBindings(new PortBinding(Ports.Binding.bindPort(8090), new ExposedPort(8080)))))
        .withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", username).withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", password)
        .withEnv("KC_HOSTNAME", url)
        .withCopyFileToContainer(MountableFile.forHostPath("../keycloak-connector-demo/docker/ivytestrealm.json"),
            "/opt/keycloak/data/import/ivytestrealm.json")
        .withCommand("start-dev --import-realm").waitingFor(Wait.forLogMessage(FINISHED_SET_UP_LOG_REGEX, 1));
    keycloakContainer.start();
  }

  @SuppressWarnings("unchecked")
  @Test
  @Order(0)
  void test_getUsers(BpmClient client) throws NoSuchFieldException {
    UserQuery query = new UserQuery();
    ExecutionResult executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client,
        RequestConstants.USER_SUB_PROCESSES, RequestConstants.GET_USER_PROCESS_NAME).execute(realmName, query);
    List<UserRepresentation> users = (List<UserRepresentation>) executionResult.data().last()
        .get(RequestConstants.USERS);
    assertEquals(1, users.size());
  }

  @Test
  @Order(1)
  void test_createNewUser(BpmClient client) throws NoSuchFieldException {
    ExecutionResult executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client,
        RequestConstants.USER_SUB_PROCESSES, RequestConstants.CREATE_USER_PROCESS_NAME)
        .execute(realmName, createMockCreateUserRequest());
    var userId = executionResult.data().last().get(RequestConstants.USER_ID);
    assertTrue(userId instanceof String);
    assertTrue(StringUtils.isNotBlank((String) userId));
  }

  @SuppressWarnings("unchecked")
  @Test
  @Order(2)
  void test_getUsersAfterCreated(BpmClient client) throws NoSuchFieldException {
    UserQuery query = new UserQuery();
    ExecutionResult executionResult = KeycloakTestUtils.getSubProcessWithNameAndPath(client,
        RequestConstants.USER_SUB_PROCESSES, RequestConstants.GET_USER_PROCESS_NAME).execute(realmName, query);
    List<UserRepresentation> users = (List<UserRepresentation>) executionResult.data().last()
        .get(RequestConstants.USERS);
    assertEquals(2, users.size());
  }

  @AfterAll
  static void clearTestContainer() {
    keycloakContainer.close();
    network.close();
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
