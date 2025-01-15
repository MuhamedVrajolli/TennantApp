package com.softco.tenantapp.tenantdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.softco.tenantapp.entity.TenantDataEntity;
import com.softco.tenantapp.exception.TenantNotFoundException;
import com.softco.tenantapp.model.TenantDataModel;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TenantDataServiceTest {

  @Mock
  private TenantDataRepository tenantDataRepository;

  @InjectMocks
  private TenantDataService tenantDataService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTenantById_tenantExists() {
    // Arrange
    String tenantId = "tenant1";
    TenantDataEntity tenantEntity = new TenantDataEntity(tenantId, "https://tenant1-service.com");
    when(tenantDataRepository.findById(tenantId)).thenReturn(Optional.of(tenantEntity));

    // Act
    TenantDataModel result = tenantDataService.getTenantById(tenantId);

    // Assert
    assertNotNull(result);
    assertEquals(tenantId, result.getTenantId());
    assertEquals("https://tenant1-service.com", result.getResourceEndpoint());
    verify(tenantDataRepository, times(1)).findById(tenantId);
  }

  @Test
  void testGetTenantById_tenantNotFound() {
    // Arrange
    String tenantId = "nonexistent";
    when(tenantDataRepository.findById(tenantId)).thenReturn(Optional.empty());

    // Act & Assert
    TenantNotFoundException exception = assertThrows(TenantNotFoundException.class,
        () -> tenantDataService.getTenantById(tenantId));
    assertEquals("Tenant with ID nonexistent not found", exception.getMessage());
    verify(tenantDataRepository, times(1)).findById(tenantId);
  }

  @Test
  void testAddTenant() {
    // Arrange
    TenantDataModel tenantModel = new TenantDataModel("tenant1", "https://tenant1-service.com");
    TenantDataEntity tenantEntity = TenantDataEntity.from(tenantModel);
    when(tenantDataRepository.save(any(TenantDataEntity.class))).thenReturn(tenantEntity);

    // Act
    TenantDataModel result = tenantDataService.addTenant(tenantModel);

    // Assert
    assertNotNull(result);
    assertEquals(tenantModel.getTenantId(), result.getTenantId());
    assertEquals(tenantModel.getResourceEndpoint(), result.getResourceEndpoint());
    verify(tenantDataRepository, times(1)).save(any(TenantDataEntity.class));
  }

  @Test
  void testRemoveTenant_tenantExists() {
    // Arrange
    String tenantId = "tenant1";
    when(tenantDataRepository.existsById(tenantId)).thenReturn(true);
    doNothing().when(tenantDataRepository).deleteById(tenantId);

    // Act
    tenantDataService.removeTenant(tenantId);

    // Assert
    verify(tenantDataRepository, times(1)).existsById(tenantId);
    verify(tenantDataRepository, times(1)).deleteById(tenantId);
  }

  @Test
  void testRemoveTenant_tenantNotFound() {
    // Arrange
    String tenantId = "nonexistent";
    when(tenantDataRepository.existsById(tenantId)).thenReturn(false);

    // Act & Assert
    TenantNotFoundException exception = assertThrows(TenantNotFoundException.class,
        () -> tenantDataService.removeTenant(tenantId));
    assertEquals("Tenant with ID nonexistent not found", exception.getMessage());
    verify(tenantDataRepository, times(1)).existsById(tenantId);
    verify(tenantDataRepository, never()).deleteById(tenantId);
  }
}

