package com.axonivy.connector.keycloak.mail;

import java.util.List;

import org.keycloak.www.client.UserRepresentation;

import ch.ivyteam.ivy.environment.Ivy;

public class MailService {

  public Mail generateApprovalMail(UserRepresentation request) {
    String senderMail = Ivy.var().get("keycloakConnector.mailSender");
    Ivy.log().warn("sender" + senderMail);
    String subject = Ivy.cms().co("/Mails/Subject");
    String body = Ivy.cms().co("/Mails/Body",
        List.of(request.getFirstName(), request.getUsername(), request.getCredentials().get(0).getValue()));
    return new Mail.MailBuilder().subject(subject).from(senderMail).to(request.getEmail()).body(body).build();
  }
}