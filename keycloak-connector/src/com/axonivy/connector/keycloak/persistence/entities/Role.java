package com.axonivy.connector.keycloak.persistence.entities;

import java.io.Serializable;

public class Role implements Serializable {
  private static final long serialVersionUID = -4241322415093698577L;
  private String id;
  private String name;

  public Role(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
