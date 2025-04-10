package com.axonivy.connector.keycloak.utils;

import org.apache.commons.lang.StringUtils;

import com.axonivy.connector.keycloak.enums.KeycloakVariable;

import ch.ivyteam.ivy.environment.Ivy;

public class VariableUtils {

  public static String getVariable(KeycloakVariable variable) {
    return variable == null ? StringUtils.EMPTY : Ivy.var().get(variable.getVariablePath());
  }
}
