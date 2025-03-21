package com.axonivy.connector.keycloak.constants;

public class RequestConstants {
  public static final String USER_SUB_PROCESSES = "connector/Users";
  public static final String AUTTHEN_SUB_PROCESSES = "connector/Authentication";
  public static final String ROLE_SUB_PROCESSES = "connector/Groups";
  public static final String GET_ROLES_START_NAME = "getGroups";
  public static final String LOGIN_START_NAME = "login";

  public static final String GET_USERS_START_NAME = "getUsers";
  public static final String GET_USERS_BY_MAIL_START_NAME = "getUsersByMail";
  public static final String CREATE_USER_START_NAME = "createUser";
  public static final String DELETE_USER_START_NAME = "deleteUser";
  public static final String UPDATE_USER_START_NAME = "updateUserStatus";
  public static final String RESET_USER_START_NAME = "updatePassword";
  public static final String UPDATE_USERS_PASSWORD_START_NAME = "updatePassword";
  public static final String CREATE_USER_PROCESS_NAME = "createUser(String,UserRepresentation)";
  public static final String GET_USERS_PROCESS_NAME = "getUsers(String)";

  public static final String REALM_NAME_PARAM = "realmName";
  public static final String USER_QUERY_PARAM = "userQuery";
  public static final String USER_ID = "userId";
  public static final String TOKEN_REQUEST_PARAM = "tokenRequest";
  public static final String ENABLE_STATUS_REQUEST_PARAM = "tokenRequest";
  public static final String REGISTRATION_APPROVAL_SIGNAL = "com:axonivy:keycloak:application:approval";
  public static final String USER = "user";
  public static final String USERS = "users";
  public static final String ROLES = "groups";
  public static final String RESPONSE_STATUS_RESULT = "responseStatusCode";
  public static final String CREDENTIAL_PARAM = "credential";
}
