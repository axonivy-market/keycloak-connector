package com.axonivy.connector.keycloak.feature.auth;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.keycloak.www.client.AuthAccessToken;
import org.keycloak.www.client.OpenidconnectTokenBody.GrantTypeEnum;

import com.axonivy.connector.keycloak.bo.TokenRequest;
import com.axonivy.connector.keycloak.constants.RequestConstants;
import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.utils.VariableUtils;

import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class KeycloakAuthFilter implements ClientRequestFilter {
  private String accessToken;
  private String refreshToken;
  private long accessTokenExpiration = 0;
  private long refreshTokenExpiration = 0;
  private final ReentrantLock tokenLock = new ReentrantLock();

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    if (requestContext.getUri().getPath().endsWith("token")) {
      return;
    }
    String token = getValidAccessToken();
    requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
  }

  private void processTokenResponse(AuthAccessToken token) {
    this.accessToken = token.getAccessToken();
    this.refreshToken = token.getRefreshToken();
    int tokeExpiresIn = token.getExpiresIn();
    this.accessTokenExpiration = System.currentTimeMillis() + (tokeExpiresIn * 1000);
    int refresTokenExpiresIn = token.getRefreshExpiresIn();
    this.refreshTokenExpiration = System.currentTimeMillis() + (refresTokenExpiresIn * 1000);
  }

  private void getToken(boolean isNewToken) {
    TokenRequest request = buildTokenRequest(isNewToken);
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.AUTTHEN_SUB_PROCESSES)
        .withStartName(RequestConstants.LOGIN_START_NAME).withParam(RequestConstants.TOKEN_REQUEST_PARAM, request)
        .call();
    AuthAccessToken token = (AuthAccessToken) Optional.ofNullable(callResult).map(result -> result.get("token"))
        .orElse(null);
    if (null != token) {
      processTokenResponse(token);
    }
  }

  private TokenRequest buildTokenRequest(boolean isNewTokenRequest) {
    TokenRequest request = new TokenRequest();
    request.setClientId("admin-cli");
    GrantTypeEnum grantType;
    if (isNewTokenRequest) {
      grantType = GrantTypeEnum.PASSWORD;
      request.setUsername(VariableUtils.getVariable(KeycloakVariable.USER_NAME));
      request.setPassword(VariableUtils.getVariable(KeycloakVariable.PASSWORD));
    } else {
      grantType = GrantTypeEnum.REFRESH_TOKEN;
      request.setRefreshToken(this.refreshToken);
    }
    request.setRealmName(VariableUtils.getVariable(KeycloakVariable.REALM_NAME));
    request.setGrantType(grantType);
    return request;
  }

  private String getValidAccessToken() {
    tokenLock.lock();
    try {
      if (accessToken != null && System.currentTimeMillis() < accessTokenExpiration) {
        return accessToken;
      }

      if (refreshToken != null && System.currentTimeMillis() < refreshTokenExpiration) {
        try {
          getToken(false);
          return accessToken;
        } catch (Exception e) {
          System.out.println("Refresh token failed, falling back to password grant: " + e.getMessage());
        }
      }
      getToken(true);
      return accessToken;
    } finally {
      tokenLock.unlock();
    }
  }
}
