package com.axonivy.connector.keycloak.auth;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class AuthorizationFilter implements ClientRequestFilter {
  private static final String AUTHORIZATION = "Authorization";


  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    if (requestContext.getHeaders().containsKey(AUTHORIZATION)) {
      return;
    }
  }

}
