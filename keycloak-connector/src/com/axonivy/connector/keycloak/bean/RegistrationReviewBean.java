package com.axonivy.connector.keycloak.bean;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.keycloak.enums.AdminDecision;
import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.persistence.entities.Role;
import com.axonivy.connector.keycloak.service.RoleServices;
import com.axonivy.connector.keycloak.utils.RoleUtils;
import com.axonivy.connector.keycloak.utils.VariableUtils;

@ViewScoped
@Named
public class RegistrationReviewBean implements Serializable {
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
