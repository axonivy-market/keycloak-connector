package com.axonivy.connector.keycloak.service;

import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;

import ch.ivyteam.ivy.environment.Ivy;

public class RegistrationService {
  private static final int DEFAULT_RETRY_CYCLE_DURATION = 1000;
  private static final int DEFAULT_RETRY_COUNT = 5;
  private static final RegistrationRepository repo = RegistrationRepository.getInstance();

  public RegistrationService() {
  }

  public boolean isEmailExisted(String mail) {
    return repo.isEmailExisted(mail);
  }

  public String save(Registration registration) {
    return repo.save(registration);
  }

  public Registration findById(String id) {
    int attempt = 0;
    while (attempt <= DEFAULT_RETRY_COUNT) {
      Registration registration = repo.findById(id);
      if (registration != null) {
        return registration;
      }

      if (attempt < DEFAULT_RETRY_COUNT) {
        try {
          Thread.sleep(DEFAULT_RETRY_CYCLE_DURATION);
          attempt++;
          Ivy.log().warn("Retry quuery registration with id {0} - attemp {1}", id, attempt);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        return null;
      }
    }
    return null;
  }
}
