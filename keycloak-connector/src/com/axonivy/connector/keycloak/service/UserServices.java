package com.axonivy.connector.keycloak.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.bo.UserQuery;
import com.axonivy.connector.keycloak.constants.ProcessPaths;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class UserServices {

  // TODO: remove comment after testing
  public static boolean isExistedUserEmail(String mail, String realmName) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail(mail).build();
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    if (callResult == null || callResult.get("error") != null
        || CollectionUtils.isNotEmpty((Collection<?>) callResult.get("users"))) {
//      return true;
    }
    return false;
  }

  public static void sendUserApprovalSignal(String applicationId) {
    Ivy.wf().signals().create().data(applicationId).makeCurrentTaskPersistent()
        .send(ProcessPaths.REGISTRATION_APPROVAL_SIGNAL);
  }

  // TODO: update this method
  @SuppressWarnings("unchecked")
  public static List<UserRepresentation> getUsersByQuery(UserQuery query) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, "")
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    return (List<UserRepresentation>) Optional.ofNullable(callResult).map(result -> result.get("user")).orElse(null);
  }

  // TODO: complete this method
  @SuppressWarnings("unchecked")
  public static UserRepresentation getUserById(String userId) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail("").build();
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, "")
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    return (UserRepresentation) Optional.ofNullable(callResult)
        .map(result -> (List<UserRepresentation>) result.get("user")).map(users -> users.get(0)).orElse(null);
  }
}
