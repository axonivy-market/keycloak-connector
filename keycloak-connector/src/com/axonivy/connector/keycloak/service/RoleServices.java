package com.axonivy.connector.keycloak.service;

import java.util.List;
import java.util.Optional;

import org.keycloak.www.client.GroupRepresentation;

import com.axonivy.connector.keycloak.constants.RequestConstants;

import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class RoleServices {

  @SuppressWarnings("unchecked")
  public List<GroupRepresentation> getRolesFromRealms(String realmName) {
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.ROLE_SUB_PROCESSES)
        .withStartName(RequestConstants.GET_ROLES_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName).call();
    return (List<GroupRepresentation>) Optional.ofNullable(callResult)
        .map(result -> result.get(RequestConstants.ROLES)).orElse(null);
  }
}
