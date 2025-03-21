package com.axonivy.connector.keycloak.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.keycloak.www.client.CredentialRepresentation;
import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.bo.UserQuery;
import com.axonivy.connector.keycloak.constants.RequestConstants;
import com.axonivy.connector.keycloak.utils.UserUtils;

import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class UserServices {

  public boolean isExistedUserEmail(String mail, String realmName) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail(mail).build();
    return CollectionUtils.isNotEmpty(getUsersByMail(query, realmName));
  }

  @SuppressWarnings("unchecked")
  public List<UserRepresentation> getUsersByMail(UserQuery query, String realmName) {
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.GET_USERS_BY_MAIL_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName)
        .withParam(RequestConstants.USER_QUERY_PARAM, query).call();
    return (List<UserRepresentation>) Optional.ofNullable(callResult)
        .map(result -> result.get(RequestConstants.USERS)).orElse(null);
  }
  
  @SuppressWarnings("unchecked")
  public List<UserRepresentation> getAllUsers(String realmName) {
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.GET_USERS_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName)
        .call();
    return (List<UserRepresentation>) Optional.ofNullable(callResult)
        .map(result -> result.get(RequestConstants.USERS)).orElse(null);
  }

  public Integer deleteUser(String realmName, String userId) {
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.DELETE_USER_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName)
        .withParam(RequestConstants.USER_ID, userId).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer resetPasswordUser(String realmName, String userId) {
    CredentialRepresentation credential = UserUtils.createTemporaryResetedPassword();
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.UPDATE_USERS_PASSWORD_START_NAME)
        .withParam(RequestConstants.REALM_NAME_PARAM, realmName).withParam(RequestConstants.USER_ID, userId)
        .withParam(RequestConstants.CREDENTIAL_PARAM, credential).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public Integer disableUser(String realmName, String userId) {
    UserRepresentation request = new UserRepresentation();
    request.setEnabled(false);
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.UPDATE_USER_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName)
        .withParam(RequestConstants.USER_ID, userId).withParam(RequestConstants.USER, request).call();
    return getResponseStatusCodeFromCallResult(callResult);
  }

  public String createUser(String realmName, UserRepresentation userRepresentation) {
    SubProcessCallResult callResult = SubProcessCall.withPath(RequestConstants.USER_SUB_PROCESSES)
        .withStartName(RequestConstants.CREATE_USER_START_NAME).withParam(RequestConstants.REALM_NAME_PARAM, realmName)
        .withParam(RequestConstants.USER, userRepresentation).call();
    return Optional.ofNullable(callResult).map(result -> (String) result.get(RequestConstants.USER_ID))
        .orElse(StringUtils.EMPTY);
  }

  private Integer getResponseStatusCodeFromCallResult(SubProcessCallResult result) {
    return Optional.ofNullable(result).map(callResult -> (Integer) callResult.get(RequestConstants.RESPONSE_STATUS_RESULT))
        .orElse(HttpStatus.SC_UNPROCESSABLE_ENTITY);
  }
}
