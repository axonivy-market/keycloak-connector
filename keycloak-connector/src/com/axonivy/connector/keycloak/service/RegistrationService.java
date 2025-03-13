package com.axonivy.connector.keycloak.service;

import java.util.UUID;

import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;

public class RegistrationService {

  public RegistrationService() {
  }

  public boolean isEmailExisted(String mail) {
    return RegistrationRepository.getInstance().isEmailExisted(mail);
  }
  
  public String save(Registration registration) {
    registration.setId(UUID.randomUUID());
    return RegistrationRepository.getInstance().save(registration);
  }
}
