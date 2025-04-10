package com.axonivy.connector.keycloak.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.StringUtils;

import com.axonivy.connector.keycloak.enums.AdminDecision;
import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.persistence.entities.Role;
import com.axonivy.connector.keycloak.service.RoleServices;
import com.axonivy.connector.keycloak.utils.RoleUtils;
import com.axonivy.connector.keycloak.utils.VariableUtils;

@ViewScoped
@ManagedBean
public class RegistrationReviewBean {
  private String errorMessage = StringUtils.EMPTY;
  private String errorSummary = StringUtils.EMPTY;
  private Boolean isValidationConfirmation;
  private List<Role> userRoles;

  @PostConstruct
  private void init() {
    String realmsName = VariableUtils.getVariable(KeycloakVariable.REALM_NAME);
    var keycloakRoles = new RoleServices().getRolesFromRealms(realmsName);
    userRoles = RoleUtils.convertToSimpleKeyCloakRoles(keycloakRoles);
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

  public AdminDecision[] getAdminDecisions() {
    return AdminDecision.values();
  }


  public Boolean getIsValidationConfirmation() {
    return isValidationConfirmation;
  }

  public void setIsValidationConfirmation(Boolean isValidationConfirmation) {
    this.isValidationConfirmation = isValidationConfirmation;
  }

  public List<Role> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<Role> userRoles) {
    this.userRoles = userRoles;
  }
}
