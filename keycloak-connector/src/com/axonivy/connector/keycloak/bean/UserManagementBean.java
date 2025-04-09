package com.axonivy.connector.keycloak.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.enums.UserStatus;
import com.axonivy.connector.keycloak.model.UserLazyDataModel;
import com.axonivy.connector.keycloak.persistence.entities.Role;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;
import com.axonivy.connector.keycloak.service.RoleServices;
import com.axonivy.connector.keycloak.utils.RoleUtils;
import com.axonivy.connector.keycloak.utils.VariableUtils;

@ViewScoped
@ManagedBean
public class UserManagementBean {
  private UserStatus[] userStatus;
  private UserLazyDataModel dataModel;
  private List<Role> userRoles;

  @PostConstruct
  public void init() {
    dataModel = new UserLazyDataModel(RegistrationRepository.getInstance());
    userStatus = UserStatus.values();
    String realmName = VariableUtils.getVariable(KeycloakVariable.REALM_NAME);
    var keycloakRoles = new RoleServices().getRolesFromRealms(realmName);
    setUserRoles(RoleUtils.convertToSimpleKeyCloakRoles(keycloakRoles));
  }

  public UserStatus[] getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus[] userStatus) {
    this.userStatus = userStatus;
  }

  public UserLazyDataModel getDataModel() {
    return dataModel;
  }

  public void setDataModel(UserLazyDataModel dataModel) {
    this.dataModel = dataModel;
  }

  public List<Role> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<Role> userRoles) {
    this.userRoles = userRoles;
  }
}
