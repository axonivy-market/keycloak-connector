package com.axonivy.connector.keycloak.persistence.entities;
import java.io.Serializable;

public class RegistrationApplication implements Serializable {
	private static final long serialVersionUID = 4238392914201477447L;
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String companyName;
	private String accountNumber;
	private String additionalInformation;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
