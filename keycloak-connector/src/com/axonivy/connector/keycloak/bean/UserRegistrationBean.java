package com.axonivy.connector.keycloak.bean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.PF;

import com.axonivy.connector.keycloak.enums.RejectReason;
import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.utils.FaceContexts;

import ch.ivyteam.ivy.environment.Ivy;

@ViewScoped
@ManagedBean
public class UserRegistrationBean {
  private Registration application;
  public static final String SHOW_DIALOG_SCRIPT = "PF('error-message').show()";
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;

  @PostConstruct
  private void init() {
    application = FaceContexts.evaluateValueExpression("#{data.registration}", Registration.class);
  }
  
  public void showMessage() {
	    RejectReason rejectReason = application.getRejectReason();
	    if (rejectReason != null) {
	    errorMessage = rejectReason == RejectReason.EXISTED_REGISTRATION ? Ivy.cms().co("/Dialogs/UserRegistration/DuplicatedApplicationMessage") : Ivy.cms().co("/Dialogs/UserRegistration/DuplicatedUserMailMessage");
	      errorSummary = rejectReason == RejectReason.EXISTED_REGISTRATION ? Ivy.cms().co("/Dialogs/UserRegistration/DuplicatedApplicationSummary") : Ivy.cms().co("/Dialogs/UserRegistration/DuplicatedUserMailSummary");
	      addMessage(errorSummary, errorMessage);
	      PF.current().ajax().update("growl");
	    }
  }
  
  public void addMessage(String summary, String detail) {
      FacesContext.getCurrentInstance().
              addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail)); 
      
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
