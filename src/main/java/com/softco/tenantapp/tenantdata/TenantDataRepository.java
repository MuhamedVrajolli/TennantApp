package com.softco.tenantapp.tenantdata;

import com.softco.tenantapp.entity.TenantDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantDataRepository extends JpaRepository<TenantDataEntity, String> {
}
