package com.softco.tenantapp.routing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.softco.tenantapp.exception.RequestProcessingException;
import com.softco.tenantapp.exception.TenantNotFoundException;
import com.softco.tenantapp.metrics.MetricsService;
import com.softco.tenantapp.model.ApiResponse;
import com.softco.tenantapp.model.TenantDataModel;
import com.softco.tenantapp.tenantdata.TenantDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class RoutingServiceTest {

  @Mock
  private TenantDataService tenantDataService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private MetricsService metricsService;

  @InjectMocks
  private RoutingService routingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRouteRequest_Success() {
    // Arrange
    String tenantId = "tenant1";
    String endpoint = "https://tenant1-service.com/resource";
    String responseBody = "Success Response";

    TenantDataModel tenantData = new TenantDataModel(tenantId, endpoint);

    when(tenantDataService.getTenantById(tenantId)).thenReturn(tenantData);
    when(restTemplate.getForEntity(endpoint, String.class)).thenReturn(ResponseEntity.ok(responseBody));

    // Act
    ApiResponse<String> result = routingService.routeRequest(tenantId);

    // Assert
    assertNotNull(result);
    assertEquals(responseBody, result.getData());
    verify(tenantDataService, times(1)).getTenantById(tenantId);
    verify(metricsService, times(1)).incrementTenantRequestCount(tenantId);
    verify(metricsService, times(1)).recordTenantRequestLatency(eq(tenantId), anyDouble());
    verify(restTemplate, times(1)).getForEntity(endpoint, String.class);
  }

  @Test
  void testRouteRequest_TenantNotFound() {
    // Arrange
    String tenantId = "nonexistent";
    when(tenantDataService.getTenantById(tenantId)).thenThrow(new TenantNotFoundException("Tenant with ID " + tenantId + " not found"));

    // Act & Assert
    TenantNotFoundException exception = assertThrows(TenantNotFoundException.class, () -> routingService.routeRequest(tenantId));
    assertEquals("Tenant with ID nonexistent not found", exception.getMessage());

    verify(tenantDataService, times(1)).getTenantById(tenantId);
    verifyNoInteractions(restTemplate, metricsService);
  }

  @Test
  void testRouteRequest_RequestFailure() {
    // Arrange
    String tenantId = "tenant1";
    String endpoint = "https://tenant1-service.com/resource";

    TenantDataModel tenantData = new TenantDataModel(tenantId, endpoint);
    when(tenantDataService.getTenantById(tenantId)).thenReturn(tenantData);
    when(restTemplate.getForEntity(endpoint, String.class)).thenThrow(new RuntimeException("Connection error"));

    // Act & Assert
    RequestProcessingException exception = assertThrows(RequestProcessingException.class, () -> routingService.routeRequest(tenantId));
    assertTrue(exception.getMessage().contains("Routing failed for tenant ID tenant1"));

    verify(tenantDataService, times(1)).getTenantById(tenantId);
    verify(metricsService, times(1)).incrementTenantRequestCount(tenantId);
    verify(metricsService, never()).recordTenantRequestLatency(eq(tenantId), anyDouble());
    verify(restTemplate, times(1)).getForEntity(endpoint, String.class);
  }
}

