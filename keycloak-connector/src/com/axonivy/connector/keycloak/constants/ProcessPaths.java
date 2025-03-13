package com.axonivy.connector.keycloak.constants;

public class ProcessPaths {
  public static final String USER_SUB_PROCESSES = "Functional Processes/Users";
  public static final String AUTTHEN_SUB_PROCESSES = "Functional Processes/Authentication";
  public static final String LOGIN_START_NAME = "getUsers";

  public static final String GET_USERS_START_NAME = "getUsers";
  public static final String CREATE_USERS_START_NAME = "createUser";
  public static final String DELETE_USERS_START_NAME = "deleteUser";
  public static final String UPDATE_USERS_START_NAME = "updateUser";
  public static final String UPDATE_USERS_PASSWORD_START_NAME = "updatePassword";
  public static final String REALM_NAME_PARAM = "realmName";
  public static final String USER_QUERY_PARAM = "userQuery";
  public static final String REQUEST_TOKEN_PARAM = "requestToken";
  public static final String REGISTRATION_APPROVAL_SIGNAL = "com:axonivy:keycloak:application:approval";
}
