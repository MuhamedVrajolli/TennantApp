package com.softco.tenantapp.tenantdata;

import com.softco.tenantapp.model.TenantDataModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/tenants")
@RequiredArgsConstructor
public class TenantDataController {

  private final TenantDataService tenantDataService;

  /**
   * Retrieves the details of a tenant by its ID.
   *
   * @param tenantId the unique ID of the tenant to retrieve.
   * @return a {@link TenantDataModel} containing the tenant details.
   */
  @GetMapping("/{tenantId}")
  public TenantDataModel getTenantById(@PathVariable String tenantId) {
    return tenantDataService.getTenantById(tenantId);
  }

  /**
   * Adds a new tenant to the system.
   *
   * @param tenantDataModel the tenant data to be added.
   * @return a {@link TenantDataModel} containing the saved tenant details.
   */
  @PostMapping
  public TenantDataModel addTenant(@RequestBody TenantDataModel tenantDataModel) {
    return tenantDataService.addTenant(tenantDataModel);
  }

  /**
   * Removes a tenant by its ID.
   *
   * @param tenantId the unique ID of the tenant to remove.
   */
  @DeleteMapping("/{tenantId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeTenant(@PathVariable String tenantId) {
    tenantDataService.removeTenant(tenantId);
  }
}
