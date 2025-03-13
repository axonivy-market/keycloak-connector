package com.axonivy.connector.keycloak.persistence.repo;

import java.util.List;

import com.axonivy.connector.keycloak.persistence.entities.Registration;

import ch.ivyteam.ivy.business.data.store.search.Query;
import ch.ivyteam.ivy.environment.Ivy;

public class RegistrationRepository {
  private static int DEFAULT_SEARCH_LIMIT = 5000;
  private static final RegistrationRepository instance = new RegistrationRepository();

  private RegistrationRepository() {
  }

  public static RegistrationRepository getInstance() {
    return instance;
  }

  public Class<Registration> getType() {
    return Registration.class;
  }

  public String save(Registration registration) {
    return Ivy.repo().save(registration).getId();
  }

  public boolean isEmailExisted(String mail) {
    return createSearchQuery().textField("email").isEqualToIgnoringCase(mail).limit(DEFAULT_SEARCH_LIMIT).execute()
        .count() != 0L;
  }

  public Registration findBymail(String mail) {
    return createSearchQuery().textField("email").isEqualToIgnoringCase(mail).limit(DEFAULT_SEARCH_LIMIT).execute()
        .getFirst();
  }

  public Query<Registration> createSearchQuery() {
    return Ivy.repo().search(getType());
  }

  public Registration findById(String id) {
    return Ivy.repo().find(id, getType());
  }
  
  public List<Registration> getQueryResult(Query<Registration> query) {
    return query.execute().getAll();
  }
}
