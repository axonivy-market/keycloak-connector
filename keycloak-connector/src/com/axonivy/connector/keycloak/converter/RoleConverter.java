package com.axonivy.connector.keycloak.converter;

import java.util.List;
import java.util.Optional;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.keycloak.bean.RegistrationReviewBean;
import com.axonivy.connector.keycloak.persistence.entities.Role;
import com.axonivy.connector.keycloak.utils.FaceContexts;

@FacesConverter(value = "roleConverter")
public class RoleConverter implements Converter {

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
    if (value == null || value.isEmpty()) {
      return null;
    }

    RegistrationReviewBean bean = FaceContexts.evaluateValueExpression("#{registrationReviewBean}",
        RegistrationReviewBean.class);
    List<Role> userRoles = bean.getUserRoles();
    return userRoles.stream().filter(role -> role.getId().equals(value)).findFirst().orElse(null);
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
    if (value == null || StringUtils.isEmpty(value.toString())) {
      return null;
    }
    return Optional.ofNullable((Role) value).map(Role::getId).orElse(null);
  }
}