package com.axonivy.connector.keycloak.utils;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.ClassUtils;

public class FaceContexts {
  public static <E> E invokeMethodByExpression(String methodExpressionLiteral, Object[] parameters,
      Class<E> returnedType) {
    ELContext elContext = getELContext();
    Application application = getApplication();
    ExpressionFactory expressionFactory = application.getExpressionFactory();
    MethodExpression methodExpression = expressionFactory.createMethodExpression(elContext, methodExpressionLiteral,
        returnedType, ClassUtils.toClass(parameters));
    E returnData = invokeMethod(elContext, methodExpression, parameters, returnedType);
    return returnData;
  }

  public static ELContext getELContext() {
    return getCurrentInstance().getELContext();
  }

  public static Application getApplication() {
    return getCurrentInstance().getApplication();
  }

  private static FacesContext getCurrentInstance() {
    return FacesContext.getCurrentInstance();
  }

  @SuppressWarnings("unchecked")
  private static <E> E invokeMethod(ELContext elContext, MethodExpression methodExpression, Object[] parameters,
      Class<E> returnedType) {
    try {
      Object result = methodExpression.invoke(elContext, parameters);
      return returnedType != null ? returnedType.cast(result) : (E) result;
    } catch (ELException | ClassCastException e) {
      throw new UnsupportedOperationException("Cannot invoke method expression", e);
    }
  }
}
