package com.axonivy.connector.keycloak.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;

import com.axonivy.connector.keycloak.persistence.entities.Registration;

@ViewScoped
@ManagedBean
public class UserRegistrationBean {
  private Registration application;
  public static final String SHOW_DIALOG_SCRIPT = "PF('error-message').show()";
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;

  @PostConstruct
  private void init() {
    application = new Registration();
  }

  public Registration getApplication() {
    return application;
  }

  public void setApplication(Registration application) {
    this.application = application;
  }

  public String getErrorSummary() {
    return errorSummary;
  }

  public void setErrorSummary(String errorSummary) {
    this.errorSummary = errorSummary;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
