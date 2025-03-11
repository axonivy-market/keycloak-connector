package com.axonivy.connector.keycloak.enums;

import java.util.stream.Stream;

public enum GrantType {
  PASSWORD("password"), REFRESH("refresh");

  private String code;

  private GrantType(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public static GrantType of(String searchValue) {
    return Stream.of(values()).filter(type -> type.code.equals(searchValue)).findAny().orElse(null);
  }
}
