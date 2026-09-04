package com.axonivy.connector.keycloak.feature.auth;

import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

public class KeycloakAuthFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new KeycloakAuthFilter(), Priorities.AUTHORIZATION);
    return true;
  }
}
