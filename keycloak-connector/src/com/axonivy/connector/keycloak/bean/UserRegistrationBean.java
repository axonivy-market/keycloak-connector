package com.axonivy.connector.keycloak.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;
import com.axonivy.connector.keycloak.persistence.entities.RegistrationApplication;

@ViewScoped
@ManagedBean
public class UserRegistrationBean {
  private RegistrationApplication application;
  public static final String SHOW_DIALOG_SCRIPT = "PF('error-message').show()";
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;

  @PostConstruct
  private void init() {
    application = new RegistrationApplication();
  }

  public RegistrationApplication getApplication() {
    return application;
  }

  public void setApplication(RegistrationApplication application) {
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
