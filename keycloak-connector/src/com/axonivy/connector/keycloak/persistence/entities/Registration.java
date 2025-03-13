package com.axonivy.connector.keycloak.persistence.entities;

import java.io.Serializable;
import java.util.UUID;

import com.axonivy.connector.keycloak.enums.AdminDecision;
import com.axonivy.connector.keycloak.enums.RejectReason;
import com.axonivy.connector.keycloak.enums.UserStatus;

public class Registration implements Serializable {
  private static final long serialVersionUID = 4238392914201477447L;
  private UUID id;
  private String userName;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String companyName;
  private String accountNumber;
  private String additionalInformation;
  private String userRole;
  private AdminDecision adminDecision;
  private RejectReason rejectReason;
  private UserStatus userStatus;
  private String comment;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public RejectReason getRejectReason() {
    return rejectReason;
  }

  public void setRejectReason(RejectReason rejectReason) {
    this.rejectReason = rejectReason;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public AdminDecision getAdminDecision() {
    return adminDecision;
  }

  public void setAdminDecision(AdminDecision adminDecision) {
    this.adminDecision = adminDecision;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "Registration [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
        + ", phoneNumber=" + phoneNumber + ", companyName=" + companyName + ", accountNumber=" + accountNumber
        + ", additionalInformation=" + additionalInformation + ", userRole=" + userRole + ", adminDecision="
        + adminDecision + ", rejectReason=" + rejectReason + ", userStatus=" + userStatus + ", comment=" + comment
        + "]";
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  
}
