package com.axonivy.connector.keycloak.persistence.entities;
import java.io.Serializable;

import com.axonivy.connector.keycloak.enums.AdminDecision;
import com.axonivy.connector.keycloak.enums.RejectReason;

public class Registration implements Serializable {
	private static final long serialVersionUID = 4238392914201477447L;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String companyName;
	private String accountNumber;
	private String additionalInformation;
	private RejectReason rejectReason;
	private AdminDecision adminDecision;

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

  public AdminDecision getAdminDecision() {
    return adminDecision;
  }

  public void setAdminDecision(AdminDecision adminDecision) {
    this.adminDecision = adminDecision;
  }
}
