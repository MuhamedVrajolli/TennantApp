package com.softco.tenantapp.entity;

import com.softco.tenantapp.model.TenantDataModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tenant_data")
public class TenantDataEntity {

  @Id
  private String tenantId;
  private String resourceEndpoint;

  public static TenantDataEntity from(TenantDataModel model) {
    var entity = new TenantDataEntity();
    entity.setTenantId(model.getTenantId());
    entity.setResourceEndpoint(model.getResourceEndpoint());

    return entity;
  }
}
