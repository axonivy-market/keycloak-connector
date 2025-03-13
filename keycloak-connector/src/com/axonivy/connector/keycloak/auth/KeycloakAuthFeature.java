package com.axonivy.connector.keycloak.auth;

import javax.ws.rs.Priorities;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class KeycloakAuthFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new KeycloakAuthFilter(), Priorities.AUTHORIZATION);
    return true;
  }
}
