package com.axonivy.connector.keycloak.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpStatus;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.bo.UserQuery;
import com.axonivy.connector.keycloak.constants.ProcessPaths;

import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class UserServices {

  public boolean isExistedUserEmail(String mail, String realmName) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail(mail).build();
    return CollectionUtils.isNotEmpty(getUsersByQuery(query, realmName));
  }

  @SuppressWarnings("unchecked")
  public List<UserRepresentation> getUsersByQuery(UserQuery query, String realmName) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    return (List<UserRepresentation>) Optional.ofNullable(callResult)
        .map(result -> result.get(ProcessPaths.USERS_RESULT)).orElse(null);
  }

  @SuppressWarnings("unchecked")
  public UserRepresentation getUserById(String userId) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail("").build();
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, "")
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    return (UserRepresentation) Optional.ofNullable(callResult)
        .map(result -> (List<UserRepresentation>) result.get(ProcessPaths.USER_RESULT)).map(users -> users.get(0))
        .orElse(null);
  }

  public Integer deleteUser(String realmName, String userId) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.DELETE_USER_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_ID_NAME_PARAM, userId).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer resetPasswordUser(String realmName, String userId) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.UPDATE_USERS_PASSWORD_START_NAME)
        .withParam(ProcessPaths.REALM_NAME_PARAM, realmName).withParam(ProcessPaths.USER_ID_NAME_PARAM, userId).withParam(realmName, userId).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer disableUser(String realmName, String userId) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.UPDATE_USER_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_ID_NAME_PARAM, userId).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  private Integer getResponseStatusCodeFromCallResult(SubProcessCallResult result) {
    return (Integer) Optional.ofNullable(result)
        .map(callResult -> (Integer) callResult.get(ProcessPaths.RESPONSE_STATUS_RESULT))
        .orElse(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }
}
