package com.axonivy.connector.keycloak.persistence.repo;

import com.axonivy.connector.keycloak.persistence.entities.RegistrationApplication;

import ch.ivyteam.ivy.business.data.store.search.Query;
import ch.ivyteam.ivy.environment.Ivy;

public class RegistrationApplicationRepository {
  private static int DEFAULT_SEARCH_LIMIT = 5000;
  private static final RegistrationApplicationRepository instance = new RegistrationApplicationRepository();

  private RegistrationApplicationRepository() {
  }

  public static RegistrationApplicationRepository getInstance() {
    return instance;
  }

  public Class<RegistrationApplication> getType() {
    return RegistrationApplication.class;
  }

  public String save(RegistrationApplication application) {
    return Ivy.repo().save(application).getId();
  }

  public boolean isEmailExisted(String mail) {
    return createSearchQuery().textField("email").isEqualToIgnoringCase(mail).limit(DEFAULT_SEARCH_LIMIT).execute()
        .count() != 0L;
  }

  private Query<RegistrationApplication> createSearchQuery() {
    return Ivy.repo().search(getType());
  }

  public RegistrationApplication findById(String id) {
    return Ivy.repo().find(id, getType());
  }
}
