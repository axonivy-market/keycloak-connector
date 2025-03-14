package com.axonivy.connector.keycloak.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import ch.ivyteam.ivy.business.data.store.search.Executor;
import ch.ivyteam.ivy.business.data.store.search.Filter;
import ch.ivyteam.ivy.business.data.store.search.OrderByField;
import ch.ivyteam.ivy.business.data.store.search.OrderByFieldOrLimit;

import static com.axonivy.connector.keycloak.constants.RegistrationFilterFields.*;
import com.axonivy.connector.keycloak.persistence.entities.Registration;
import com.axonivy.connector.keycloak.persistence.repo.RegistrationRepository;

import ch.ivyteam.ivy.business.data.store.search.Query;

public class UserLazyDataModel extends LazyDataModel<Registration> {

  public UserLazyDataModel(RegistrationRepository repo) {
    super();
    this.repo = repo;
  }

  private static final long serialVersionUID = 1L;
  private RegistrationRepository repo;

  @Override
  public Registration getRowData(String rowKey) {
    return RegistrationRepository.getInstance().findBymail(rowKey);
  }

  @Override
  public String getRowKey(Registration registration) {
    return registration.getEmail();
  }

  @Override
  public List<Registration> load(int first, int pageSize, Map<String, SortMeta> sortBy,
      Map<String, FilterMeta> filterBy) {
    List<Registration> registrations = new ArrayList<>();
    Executor<Registration> executor = convertSortAndFilterCriteriaToQuery(first, pageSize, sortBy, filterBy);
    List<Registration> results = repo.getExecutorResult(executor);
    if (CollectionUtils.isNotEmpty(results)) {
      registrations.addAll(results);
    }
    Timestamp.valueOf(LocalDateTime.MIN);
    return registrations;
  }

  @Override
  public int count(Map<String, FilterMeta> filterBy) {
    Query<Registration> searchQuery = repo.createSearchQuery();
    Filter<Registration> filterQuery = null;
    if (MapUtils.isNotEmpty(filterBy)) {
      for (Entry<String, FilterMeta> entry : filterBy.entrySet()) {
        String fieldName = getQueryParamNameFromKey(entry.getKey());
        Filter<Registration> filter = repo.createSearchQuery().textField(fieldName)
            .containsPhrase(entry.getValue().getFilterValue().toString());
        filterQuery = filterQuery == null ? searchQuery.filter(filter) : filterQuery.and().filter(filter);
      }
    }
    var query = filterQuery == null ? searchQuery : filterQuery;

    return (int) query.execute().count();
  }

  private Executor<Registration> convertSortAndFilterCriteriaToQuery(int first, int pageSize,
      Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
    Query<Registration> searchQuery = repo.createSearchQuery();
    Filter<Registration> filterQuery = null;
    if (MapUtils.isNotEmpty(filterBy)) {
      for (Entry<String, FilterMeta> entry : filterBy.entrySet()) {
        String fieldName = getQueryParamNameFromKey(entry.getKey());
        Filter<Registration> filter = repo.createSearchQuery().textField(fieldName)
            .containsPhrase(entry.getValue().getFilterValue().toString());
        filterQuery = filterQuery == null ? searchQuery.filter(filter) : filterQuery.and().filter(filter);
      }
    }
    var query = filterQuery == null ? searchQuery : filterQuery;
    return applySorting(query.orderBy(), sortBy).limit(first, pageSize);
  }

  private OrderByFieldOrLimit<Registration> applySorting(OrderByField<Registration> oderBy,
      Map<String, SortMeta> sortBy) {

    if (MapUtils.isEmpty(sortBy)) {
      return oderBy.field("username").ascending();
    }

    Map.Entry<String, SortMeta> firstEntry = sortBy.entrySet().iterator().next();
    String fieldName = getQueryParamNameFromKey(firstEntry.getKey());
    SortOrder sortOrder = firstEntry.getValue().getOrder();

    return sortOrder == SortOrder.ASCENDING ? oderBy.field(fieldName).ascending()
        : oderBy.field(fieldName).descending();
  }

  private String getQueryParamNameFromKey(String metaKey) {
    return switch (metaKey) {
    case USERNAME -> "username";
    case MAIL -> "email";
    case FIRSTNAME -> "firstName";
    case LASTNAME -> "lastName";
    case ROLE -> "role";
    case STATUS -> "status";
    default -> "";
    };
  }

}
