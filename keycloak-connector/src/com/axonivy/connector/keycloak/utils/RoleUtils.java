package com.axonivy.connector.keycloak.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.keycloak.www.client.RoleRepresentation;

import com.axonivy.connector.keycloak.persistence.entities.Role;

public class RoleUtils {
  public static List<Role> convertToSimpleKeyCloakRoles(List<RoleRepresentation> roles) {
    List<Role> result = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(roles)) {
      roles.stream().forEach(role -> result.add(convertToSimpleKeyCloakRole(role)));
    }
    return result;

  }

  private static Role convertToSimpleKeyCloakRole(RoleRepresentation role) {
    return new Role(role.getId(), role.getName());
  }

}
