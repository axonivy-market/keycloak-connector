package com.axonivy.connector.keycloak.mail;

public class Mail {
  private String from;
  private String to;
  private String cc;
  private String subject;
  private String body;

  private Mail(MailBuilder builder) {
    this.from = builder.from;
    this.to = builder.to;
    this.cc = builder.cc;
    this.subject = builder.subject;
    this.body = builder.body;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public static class MailBuilder {
    private String from;
    private String to;
    private String cc;
    private String subject;
    private String body;

    public MailBuilder() {
    }

    public MailBuilder from(String from) {
      this.from = from;
      return this;
    }

    public MailBuilder to(String to) {
      this.to = to;
      return this;
    }

    public MailBuilder cc(String cc) {
      this.cc = cc;
      return this;
    }

    public MailBuilder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public MailBuilder body(String body) {
      this.body = body;
      return this;
    }

    public Mail build() {
      if (from == null || to == null || subject == null || body == null) {
        throw new IllegalStateException("Required fields (from, to, subject, body) must not be null");
      }
      return new Mail(this);
    }
  }
}
