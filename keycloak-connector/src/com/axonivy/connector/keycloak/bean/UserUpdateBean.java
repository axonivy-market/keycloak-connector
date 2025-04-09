package com.axonivy.connector.keycloak.bean;

import java.io.Serializable;
import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import com.axonivy.connector.keycloak.enums.KeycloakVariable;
import com.axonivy.connector.keycloak.enums.UserStatus;
import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;
import com.axonivy.connector.keycloak.service.UserServices;
import com.axonivy.connector.keycloak.utils.FaceContexts;
import com.axonivy.connector.keycloak.utils.VariableUtils;

import ch.ivyteam.ivy.environment.Ivy;

@ManagedBean
@ViewScoped
public class UserUpdateBean implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String userId;
  private UserServices service;
  private String realmName;
  protected static final String LOGIC_CLOSE_METHOD = "#{logic.close}";
  RegistrationRepository repo = RegistrationRepository.getInstance();
  Registration registration;

  public void init(Registration registration) {
    this.registration = registration;
    userId = registration.getUserId();
    service = new UserServices();
    realmName = VariableUtils.getVariable(KeycloakVariable.REALM_NAME);
  }

  public void delete() {
    Ivy.log().warn("deleted");
    service.deleteUser(realmName, userId);
    registration.setUserStatus(UserStatus.DELETED);
    repo.save(registration);

    closeDialog();
  }

  public int disable() {
    Ivy.log().warn("lock");
    service.disableUser(realmName, userId);

    registration.setUserStatus(UserStatus.LOCKED);
    repo.save(registration);
    closeDialog();
    return 1;
  }

  public void reset() {
    Ivy.log().warn("reset");
    service.resetPasswordUser(realmName, userId);
    closeDialog();
  }

  public boolean isUserNotExist() {
    UserStatus status = Optional.ofNullable(registration).map(Registration::getUserStatus).orElse(null);
    return status == null ? true : !UserStatus.getValidUserStatus().contains(status);
  }

  private void closeDialog() {
    FaceContexts.invokeMethodByExpression(LOGIC_CLOSE_METHOD, new Object[] {}, null);
  }
}
