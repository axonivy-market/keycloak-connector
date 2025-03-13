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
import ch.ivyteam.ivy.environment.Ivy;


public class UserLazyDataModel extends LazyDataModel<Registration> {

  private static final long serialVersionUID = 1L;
  private static RegistrationRepository repo = RegistrationRepository.getInstance();

  @Override
  public Registration getRowData(String rowKey) {
    return RegistrationRepository.getInstance().findBymail(rowKey);
  }

  @Override
  public String getRowKey(Registration registration) {
    return registration.getEmail();
  }

  // TODO: convert sorting & filter to user query
  @Override
  public List<Registration> load(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    sortBy.keySet().stream().forEach(a -> Ivy.log().warn("sortBy "+a));
    filterBy.keySet().stream().forEach(a -> Ivy.log().warn("filterBy "+a));
    Ivy.log().warn("load");

    List<Registration> registrations = new ArrayList<>();
    Query<Registration> query = convertSortAndFilterCriteriaToQuery(first, pageSize, sortBy, filterBy);
    List<Registration> results = List.of(RegistrationRepository.getInstance().findBymail("work.with.dino.888@gmail.com"));
//        repo.getQueryResult(query);
    if (CollectionUtils.isNotEmpty(results)) {
      registrations.addAll(results);
    }
    return registrations;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    // TODO Auto-generated method stub
    return 1;
  }

  // TODO: convert search criteria to user query of keycloak
  private Query<Registration> convertSortAndFilterCriteriaToQuery(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    Query<Registration> query = repo.createSearchQuery();
    query.limit(first, pageSize);
    return query;
  }

}
