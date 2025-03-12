package com.axonivy.connector.keycloak.enums;

import java.util.List;
import java.util.stream.Stream;

public enum UserStatus implements HasCmsName {
	REQUESTED("REQUESTED"),
	REJECTED("REJECTED"),
	ACTIVE("ACTIVE"),
	LOCKED("LOCKED");
	
	private String cmsName;
	
	UserStatus(String name) {
		this.cmsName = this.getCms(name);
	}

	@Override
	public String cmsRootPath() {
		return "/uk.phd.core/Enums/UserStatus";
	}

	public String getCmsName() {
		return cmsName;
	}

	public void setCmsName(String cmsName) {
		this.cmsName = cmsName;
	}
	
	public static UserStatus fromValue(String value) {
		return Stream.of(values()).filter(it -> it.name().equals(value)).findFirst().orElse(null);
	}
	
	public static List<UserStatus> getValidUserStatus(){
		return List.of(UserStatus.ACTIVE, UserStatus.LOCKED);
	}
}
