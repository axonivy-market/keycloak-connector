package com.axonivy.connector.keycloak.service;

import com.axonivy.connector.keycloak.bo.UserQuery;
import com.axonivy.connector.keycloak.constants.ProcessPaths;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

public class UserServices {
  public static boolean isExistedUserEmail(String mail, String realmName) {
    UserQuery query = new UserQuery.UserQueryBuilder().setEMail(mail).build();
    SubProcessCallResult callResult = SubProcessCall.withPath(ProcessPaths.USER_SUB_PROCESSES)
        .withStartName(ProcessPaths.GET_USERS_START_NAME).withParam(ProcessPaths.REALM_NAME_PARAM, realmName)
        .withParam(ProcessPaths.USER_QUERY_PARAM, query).call();
    if (callResult != null) {
      return true;

    }
    return false;
  }
  
  public static void sendUserApprovalSignal(String applicationId) {
    Ivy.wf().signals().create().data(applicationId).makeCurrentTaskPersistent()
        .send(ProcessPaths.REGISTRATION_APPROVAL_SIGNAL);
  }
}
