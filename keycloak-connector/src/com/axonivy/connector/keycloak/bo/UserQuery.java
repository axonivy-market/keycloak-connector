package com.axonivy.connector.keycloak.bo;

public class UserQuery {
  private String eMail;
  private Boolean emailVerified;
  private Boolean enabled;
  private Boolean exact;
  private Integer first;
  private String firstName;
  private String idpAlias;
  private String idpUserId;
  private String lastName;
  private Integer max;
  private String q;
  private String search;
  private String username;

  public UserQuery() {
  }

//  public UserQuery(String eMail, Boolean emailVerified, Boolean enabled, Boolean exact, Integer first, String firstName,
//      String idpAlias, String idpUserId, String lastName, Integer max, String q, String search, String username) {
//    super();
//    this.eMail = eMail;
//    this.emailVerified = emailVerified;
//    this.enabled = enabled;
//    this.exact = exact;
//    this.first = first;
//    this.firstName = firstName;
//    this.idpAlias = idpAlias;
//    this.idpUserId = idpUserId;
//    this.lastName = lastName;
//    this.max = max;
//    this.q = q;
//    this.search = search;
//    this.username = username;
//  }

  public UserQuery(UserQueryBuilder userQueryBuilder) {
    this.eMail = userQueryBuilder.eMail;
    this.emailVerified = userQueryBuilder.emailVerified;
    this.enabled = userQueryBuilder.enabled;
    this.exact = userQueryBuilder.exact;
    this.first = userQueryBuilder.first;
    this.firstName = userQueryBuilder.firstName;
    this.idpAlias = userQueryBuilder.idpAlias;
    this.idpUserId = userQueryBuilder.idpUserId;
    this.lastName = userQueryBuilder.lastName;
    this.max = userQueryBuilder.max;
    this.q = userQueryBuilder.q;
    this.search = userQueryBuilder.search;
    this.username = userQueryBuilder.username;
  }

  public String getEMail() {
    return eMail;
  }

  public void setEMail(String eMail) {
    this.eMail = eMail;
  }

  public Boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getExact() {
    return exact;
  }

  public void setExact(Boolean exact) {
    this.exact = exact;
  }

  public Integer getFirst() {
    return first;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getIdpAlias() {
    return idpAlias;
  }

  public void setIdpAlias(String idpAlias) {
    this.idpAlias = idpAlias;
  }

  public String getIdpUserId() {
    return idpUserId;
  }

  public void setIdpUserId(String idpUserId) {
    this.idpUserId = idpUserId;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public static class UserQueryBuilder {
    private String eMail;
    private Boolean emailVerified;
    private Boolean enabled;
    private Boolean exact;
    private Integer first;
    private String firstName;
    private String idpAlias;
    private String idpUserId;
    private String lastName;
    private Integer max;
    private String q;
    private String search;
    private String username;

    public UserQueryBuilder setEMail(String eMail) {
      this.eMail = eMail;
      return this;
    }

    public UserQueryBuilder setEmailVerified(Boolean emailVerified) {
      this.emailVerified = emailVerified;
      return this;
    }

    public UserQueryBuilder setEnabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public UserQueryBuilder setExact(Boolean exact) {
      this.exact = exact;
      return this;
    }

    public UserQueryBuilder setFirst(Integer first) {
      this.first = first;
      return this;
    }

    public UserQueryBuilder setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public UserQueryBuilder setLastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public UserQueryBuilder setIdpAlias(String idpAlias) {
      this.idpAlias = idpAlias;
      return this;
    }

    public UserQueryBuilder setIdpUserId(String idpUserId) {
      this.idpUserId = idpUserId;
      return this;
    }

    public UserQueryBuilder setMax(Integer max) {
      this.max = max;
      return this;
    }

    public UserQueryBuilder setQ(String query) {
      this.q = query;
      return this;
    }

    public UserQueryBuilder setSearch(String search) {
      this.search = search;
      return this;
    }

    public UserQueryBuilder setUsername(String username) {
      this.username = username;
      return this;
    }

    public UserQuery build() {
      return new UserQuery(this);
    }
  }

}
