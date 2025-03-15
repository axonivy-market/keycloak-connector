package com.axonivy.connector.keycloak.mail;

import java.util.List;

import org.keycloak.www.client.UserRepresentation;

import ch.ivyteam.ivy.environment.Ivy;

public class MailService {

  public Mail generateApprovalMail(UserRepresentation request) {
    String senderMail = Ivy.var().get("keycloakConnector.mailSender");
    String body = Ivy.cms().co("/Mails/Body",
        List.of(request.getFirstName(), request.getUsername(), request.getCredentials().get(0).getValue()));
    return new Mail.MailBuilder().from(senderMail).to(request.getEmail()).body(body).build();
  }
}