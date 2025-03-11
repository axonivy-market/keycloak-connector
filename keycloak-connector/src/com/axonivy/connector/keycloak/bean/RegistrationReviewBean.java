package com.axonivy.connector.keycloak.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;

import com.axonivy.connector.keycloak.enums.AdminDecision;
import com.axonivy.connector.keycloak.persistence.entities.RegistrationApplication;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationApplicationRepository;
import com.axonivy.connector.keycloak.utils.FaceContexts;

@ViewScoped
@ManagedBean
public class RegistrationReviewBean {
  private RegistrationApplication application;
  public static final String SHOW_DIALOG_SCRIPT = "PF('error-message').show()";
  private static final String APPLICATION_ID= "#{data.applicationId}";
  private static final String SUBMIT_LOGIC  = "";
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;
  private AdminDecision adminDecision;
  private String userRole;
  private String comment;
  private Boolean isValidationConfirmation;

  @PostConstruct
  private void init() {
    String applicationId = FaceContexts.evaluateValueExpression(APPLICATION_ID, String.class);
    application = RegistrationApplicationRepository.getInstance().findById(applicationId);
  }
  
  public void submit() {
    
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

  public AdminDecision getAdminDecision() {
    return adminDecision;
  }

  public void setAdminDecision(AdminDecision adminDecision) {
    this.adminDecision = adminDecision;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public List<String> getUserRoles() {
    return List.of("admin", "user");
  }

  public AdminDecision[] getAdminDecisions() {
    return AdminDecision.values();
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Boolean getIsValidationConfirmation() {
    return isValidationConfirmation;
  }

  public void setIsValidationConfirmation(Boolean isValidationConfirmation) {
    this.isValidationConfirmation = isValidationConfirmation;
  }
}
