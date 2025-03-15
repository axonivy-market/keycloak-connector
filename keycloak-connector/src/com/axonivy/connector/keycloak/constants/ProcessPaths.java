package com.axonivy.connector.keycloak.constants;

public class ProcessPaths {
  public static final String USER_SUB_PROCESSES = "connector/Users";
  public static final String AUTTHEN_SUB_PROCESSES = "connector/Authentication";
  public static final String ROLE_SUB_PROCESSES = "connector/Groups";
  public static final String GET_ROLES_START_NAME = "getGroups";
  public static final String LOGIN_START_NAME = "login";
  public static final String GET_USERS_START_NAME = "getUsers";
  public static final String CREATE_USERS_START_NAME = "createUser";
  public static final String DELETE_USERS_START_NAME = "deleteUser";
  public static final String UPDATE_USERS_START_NAME = "updateUser";
  public static final String UPDATE_USERS_PASSWORD_START_NAME = "updatePassword";
  public static final String REALM_NAME_PARAM = "realmName";
  public static final String USER_QUERY_PARAM = "userQuery";
  public static final String TOKEN_REQUEST_PARAM = "tokenRequest";
  public static final String REGISTRATION_APPROVAL_SIGNAL = "com:axonivy:keycloak:application:approval";
  public static final String USER_RESULT = "user";
  public static final String USERS_RESULT = "users";
  public static final String ROLES_RESULT = "groups";
}
