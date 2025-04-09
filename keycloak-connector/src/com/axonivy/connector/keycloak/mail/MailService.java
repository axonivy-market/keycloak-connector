package com.axonivy.connector.keycloak.mail;

import java.util.List;

import org.keycloak.www.client.UserRepresentation;

import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.utils.VariableUtils;

import ch.ivyteam.ivy.environment.Ivy;

public class MailService {

  public Mail generateApprovalMail(UserRepresentation request) {
    String senderMail = VariableUtils.getVariable(KeycloakVariable.APP_MAIL);
    String subject = Ivy.cms().co("/Mails/Subject");
    String body = Ivy.cms().co("/Mails/Body",
        List.of(request.getFirstName(), request.getUsername(), request.getCredentials().get(0).getValue()));
    return new Mail.MailBuilder().subject(subject).from(senderMail).to(request.getEmail()).body(body).build();
  }
}