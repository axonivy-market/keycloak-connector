package com.axonivy.connector.keycloak.bo;

import org.keycloak.www.client.OpenidconnectTokenBody.GrantTypeEnum;

public class TokenRequest {
  private String clientId;
  private String username;
  private String password;
  private String refreshToken;
  private GrantTypeEnum grantType;
  private String realmName;
  private String clientSecret;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public GrantTypeEnum getGrantType() {
    return grantType;
  }

  public void setGrantType(GrantTypeEnum grantType) {
    this.grantType = grantType;
  }

  public String getRealmName() {
    return realmName;
  }

  public void setRealmName(String realmName) {
    this.realmName = realmName;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

}
