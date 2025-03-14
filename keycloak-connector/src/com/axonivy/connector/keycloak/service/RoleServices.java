package com.axonivy.connector.keycloak.service;

import java.util.List;
import java.util.Optional;

import org.keycloak.www.client.RoleRepresentation;

import com.axonivy.connector.keycloak.constants.ProcessPaths;

import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class RoleServices {

  @SuppressWarnings("unchecked")
  public List<RoleRepresentation> getRolesFromRealms(String realmName) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.ROLE_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_ROLES_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName).call();
    return (List<RoleRepresentation>) Optional.ofNullable(callResult).map(result -> result.get(ProcessPaths.ROLES_RESULT)).orElse(null);
  }
}
