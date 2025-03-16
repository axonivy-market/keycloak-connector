package com.axonivy.connector.keycloak.bo;

public class EnableStatusRequest {
  private boolean enabled;

  public EnableStatusRequest(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
