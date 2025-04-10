package com.axonivy.connector.keycloak.enums;

public enum AdminDecision implements HasCmsName {
  GRANT_ACCESS, REJECT_ACCESS;

  @Override
  public String cmsRootPath() {
    return "/Enums/AdminDecision";
  }
}
