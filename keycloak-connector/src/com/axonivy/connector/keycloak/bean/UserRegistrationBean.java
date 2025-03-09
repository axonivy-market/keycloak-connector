package com.axonivy.connector.keycloak.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;
import org.primefaces.PF;

import com.axonivy.connector.keycloak.persistence.entities.RegistrationApplication;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationApplicationRepository;
import com.axonivy.connector.keycloak.utils.FaceContexts;

import ch.ivyteam.ivy.environment.Ivy;

@ViewScoped
@ManagedBean
public class UserRegistrationBean {
  private RegistrationApplication application;
  private final String REGISTER_LOGIC = "#{logic.register}";
  public static final String SHOW_DIALOG_SCRIPT = "PF('error-message').show()";
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;
  private RegistrationApplicationRepository applicationRepo;

  @PostConstruct
  private void init() {
    application = new RegistrationApplication();
    applicationRepo = RegistrationApplicationRepository.getInstance();
  }

  public RegistrationApplication getApplication() {
    return application;
  }

  public void setApplication(RegistrationApplication application) {
    this.application = application;
  }

  public void register() {
    if (isUserCreated()) {
      showErrorMessage(Ivy.cms().co("/Dialogs/RegistrationConfirmation/DupllicatedUserMailSummary"),
          Ivy.cms().co("/Dialogs/RegistrationConfirmation/DupllicatedUserMailMessage"));
      return;
    }

    if (isApplicationExisted()) {
      showErrorMessage(Ivy.cms().co("/Dialogs/RegistrationConfirmation/DuplicatedApplicationSummary"),
          Ivy.cms().co("/Dialogs/RegistrationConfirmation/DuplicatedApplicationMessage"));
      return;
    }
    String applicationId = applicationRepo.save(application);
    FaceContexts.invokeMethodByExpression(REGISTER_LOGIC, new Object[] { applicationId }, null);
  }

  private void showErrorMessage(String errorSummary, String errorMessage) {
    this.errorSummary = errorSummary;
    this.errorMessage = errorMessage;
    PF.current().executeScript(SHOW_DIALOG_SCRIPT);
  }

  private boolean isApplicationExisted() {
    return applicationRepo.isEmailExisted(application.getEmail());
  }

  private boolean isUserCreated() {
    return false;
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
