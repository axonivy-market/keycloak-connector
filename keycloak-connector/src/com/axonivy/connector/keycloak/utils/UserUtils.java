package com.axonivy.connector.keycloak.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.www.client.AdminrealmsCredentials;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.helper.PasswordGeneratorHelper;
import com.axonivy.connector.keycloak.persistence.entities.Registration;

public class UserUtils {
  public static final String DEFAULT_CREDENTIAL_TYPE = "password";

  public static UserRepresentation convertRegistrationToUser(Registration registration) {
    UserRepresentation user = new UserRepresentation();
    List<AdminrealmsCredentials> credentials = List.of(createTemporaryPassword());
    user.setCredentials(credentials);
    user.setUsername(StringUtils.lowerCase(registration.getEmail()));
    user.setFirstName(registration.getFirstName());
    user.setLastName(registration.getLastName());
    user.setEmail(registration.getEmail());
    user.setEnabled(true);
    return user;
  }

  private static AdminrealmsCredentials createTemporaryPassword() {
    AdminrealmsCredentials credential = new AdminrealmsCredentials();
    credential.setType(DEFAULT_CREDENTIAL_TYPE);
    credential.setValue(PasswordGeneratorHelper.generatePassword());
    credential.setTemporary(true);
    return credential;
  }

}
