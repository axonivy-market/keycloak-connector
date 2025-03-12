package com.axonivy.connector.keycloak.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;

import ch.ivyteam.ivy.business.data.store.search.Query;

public class UserLazyDataModel extends LazyDataModel<Registration> {

  private static final long serialVersionUID = 1L;
  private RegistrationRepository repo;

  @Override
  public Registration getRowData(String rowKey) {
    return repo.findBymail(rowKey);
  }

  @Override
  public String getRowKey(Registration registration) {
    return registration.getAccountNumber();
  }

  // TODO: convert sorting & filter to user query
  @Override
  public List<Registration> load(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    List<Registration> registration = new ArrayList<>();
    Query<Registration> query = convertSortAndFilterCriteriaToQuery(first, pageSize, sortBy, filterBy);
    List<Registration> results = repo.getQueryResult(query);
    if (CollectionUtils.isNotEmpty(results)) {
      registration.addAll(results);
    }
    return registration;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    // TODO Auto-generated method stub
    return 0;
  }

  // TODO: convert search criteria to user query of keycloak
  private Query<Registration> convertSortAndFilterCriteriaToQuery(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    Query<Registration> query = repo.createSearchQuery();
    return query;
  }
}
