package com.softco.tenantapp.model;

import com.softco.tenantapp.entity.TenantDataEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantDataModel {

  private String tenantId;
  private String resourceEndpoint;

  public static TenantDataModel from(TenantDataEntity entity) {
    var tenantDataModel = new TenantDataModel();
    tenantDataModel.setTenantId(entity.getTenantId());
    tenantDataModel.setResourceEndpoint(entity.getResourceEndpoint());

    return tenantDataModel;
  }
}
