package com.axonivy.connector.keycloak.enums;

public enum KeycloakVariable {
  REALM_NAME("keycloakConnector.realmName"), USER_NAME("keyCloakConnector.username"),
  PASSWORD("keyCloakConnector.passWord"), APP_MAIL("keycloakConnector.mailSender");

  private String variablePath;

  KeycloakVariable(String variablePath) {
    this.variablePath = variablePath;
  }

  public String getVariablePath() {
    return variablePath;
  }

  public void setVariablePath(String variablePath) {
    this.variablePath = variablePath;
  }
}
