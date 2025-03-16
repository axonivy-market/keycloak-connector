package com.axonivy.connector.keycloak.utils;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.www.client.AdminrealmsCredentials;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.helper.PasswordGeneratorHelper;
import com.axonivy.connector.keycloak.persistence.entities.Registration;

public class UserUtils {
  public static final String DEFAULT_CREDENTIAL_TYPE = "password";
  private static final String USER_ID_PREFIX = "users/";

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

  public static String extractUserId(Response response) {
    URI location = response.getLocation();
    if (location == null) {
      return StringUtils.EMPTY;
    }
    String path = location.getPath();
    return path.substring(path.lastIndexOf(USER_ID_PREFIX) + USER_ID_PREFIX.length());
  }

}
