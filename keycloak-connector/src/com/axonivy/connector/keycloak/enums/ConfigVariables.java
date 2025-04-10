package com.axonivy.connector.keycloak.enums;

public enum ConfigVariables {
  USERNAME("keycloakConnector.username"), PASSWORD("keycloakConnector.password"), URL("keycloakConnector.url"),
  REALM_NAME("keycloakConnector.realmName"), SENDER_MAIL("keycloakConnector.senderMail");

  private String value;

  ConfigVariables(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}