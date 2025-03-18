package com.axonivy.connector.keycloak.test.utils;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.engine.client.sub.SubRequestBuilder;

public class KeycloakTestUtils {

  private KeycloakTestUtils() {
  }

  public static SubRequestBuilder getSubProcessWithNameAndPath(BpmClient client, String subProcessPath,
      String subProcessName) {
    return client.start().subProcess(BpmProcess.path(subProcessPath).elementName(subProcessName));
  }
}