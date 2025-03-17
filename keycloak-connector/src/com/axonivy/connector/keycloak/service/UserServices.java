package com.axonivy.connector.keycloak.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.keycloak.www.client.CredentialRepresentation;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.bo.UserQuery;
import com.axonivy.connector.keycloak.constants.ProcessPaths;
import com.axonivy.connector.keycloak.utils.UserUtils;

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

  public Integer deleteUser(String realmName, String userId) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.DELETE_USER_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_ID, userId).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer resetPasswordUser(String realmName, String userId) {
    CredentialRepresentation credential = UserUtils.createTemporaryResetedPassword();
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.UPDATE_USERS_PASSWORD_START_NAME)
        .withParam(ProcessPaths.REALM_NAME_PARAM, realmName).withParam(ProcessPaths.USER_ID, userId)
        .withParam(ProcessPaths.CREDENTIAL_PARAM, credential).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer disableUser(String realmName, String userId) {
    UserRepresentation request = new UserRepresentation();
    request.setEnabled(false);
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.UPDATE_USER_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_ID, userId).withParam(ProcessPaths.USER, request).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public String createUser(String realmName, UserRepresentation userRepresentation) {
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.CREATE_USER_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER, userRepresentation).call();
    return Optional.ofNullable(callResult).map(result -> (String) result.get(ProcessPaths.USER_ID))
        .orElse(StringUtils.EMPTY);
  }

  private Integer getResponseStatusCodeFromCallResult(SubProcessCallResult result) {
    return Optional.ofNullable(result).map(callResult -> (Integer) callResult.get(ProcessPaths.RESPONSE_STATUS_RESULT))
        .orElse(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }
}
