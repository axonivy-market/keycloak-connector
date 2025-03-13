package com.axonivy.connector.keycloak.bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.axonivy.connector.keycloak.enums.UserStatus;
import com.axonivy.connector.keycloak.model.UserLazyDataModel;


@ManagedBean
@ViewScoped
public class UserManagementBean {
  private UserStatus[] userStatus;
  private UserLazyDataModel dataModel;

  @PostConstruct
  public void init() {
    dataModel = new UserLazyDataModel();
    userStatus = UserStatus.values();
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

}
