package com.softco.tenantapp.tenantdata;

import com.softco.tenantapp.entity.TenantDataEntity;
import com.softco.tenantapp.exception.TenantNotFoundException;
import com.softco.tenantapp.model.TenantDataModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TenantDataService {

  private final TenantDataRepository tenantDataRepository;

  /**
   * Retrieves a tenant by its ID.
   *
   * @param tenantId the unique ID of the tenant to retrieve.
   * @return a {@link TenantDataModel} representing the tenant details.
   * @throws RuntimeException if the tenant with the specified ID does not exist.
   */
  public TenantDataModel getTenantById(String tenantId) {
    log.debug("Fetching tenant with ID: {}", tenantId);
    return tenantDataRepository.findById(tenantId)
        .map(TenantDataModel::from)
        .orElseThrow(() -> new TenantNotFoundException("Tenant with ID " + tenantId + " not found"));
  }

  /**
   * Adds a new tenant to the system.
   *
   * @param tenantDataModel the tenant data to be added.
   * @return a {@link TenantDataModel} representing the saved tenant details.
   */
  public TenantDataModel addTenant(TenantDataModel tenantDataModel) {
    log.debug("Adding new tenant with ID: {}", tenantDataModel.getTenantId());
    var savedEntity = tenantDataRepository.save(TenantDataEntity.from(tenantDataModel));
    return TenantDataModel.from(savedEntity);
  }

  /**
   * Removes a tenant by its ID.
   *
   * @param tenantId the unique ID of the tenant to be removed.
   * @throws RuntimeException if the tenant with the specified ID does not exist.
   */
  public void removeTenant(String tenantId) {
    log.debug("Removing tenant with ID: {}", tenantId);
    if (tenantDataRepository.existsById(tenantId)) {
      tenantDataRepository.deleteById(tenantId);
    } else {
      throw new TenantNotFoundException("Tenant with ID " + tenantId + " not found");
    }
  }
}
