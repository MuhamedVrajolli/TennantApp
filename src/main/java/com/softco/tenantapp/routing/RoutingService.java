package com.softco.tenantapp.routing;

import com.softco.tenantapp.exception.RequestProcessingException;
import com.softco.tenantapp.metrics.MetricsService;
import com.softco.tenantapp.model.ApiResponse;
import com.softco.tenantapp.model.TenantDataModel;
import com.softco.tenantapp.tenantdata.TenantDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {

  private final TenantDataService tenantDataService;
  private final RestTemplate restTemplate;
  private final MetricsService metricsService;

  /**
   * Routes a GET request to the tenant-specific endpoint.
   *
   * @param tenantId the unique ID of the tenant for which the request is being routed.
   * @return an {@link ApiResponse} containing the response body as a string.
   * @throws RuntimeException if the tenant data is not found or the request fails.
   */
  public ApiResponse<String> routeRequest(String tenantId) {
    log.info("Routing request for tenant ID: {}", tenantId);

    // Fetch tenant data
    TenantDataModel tenantData = tenantDataService.getTenantById(tenantId);

    long startTime = System.currentTimeMillis(); // Start measuring latency
    metricsService.incrementTenantRequestCount(tenantId); // Record request count

    try {
      log.debug("Making GET request to endpoint: {}", tenantData.getResourceEndpoint());
      // Make the GET request
      ResponseEntity<String> response = restTemplate.getForEntity(tenantData.getResourceEndpoint(), String.class);

      long latency = System.currentTimeMillis() - startTime;
      metricsService.recordTenantRequestLatency(tenantId, latency); // Record latency

      log.info("Request successfully routed for tenant ID: {}, Response: {}", tenantId, response.getBody());
      return new ApiResponse<>(response.getBody()); // Return the response body
    } catch (Exception e) {
      log.error("Routing failed for tenant ID: {}, Error: {}", tenantId, e.getMessage(), e);
      throw new RequestProcessingException("Routing failed for tenant ID " + tenantId + ": " + e.getMessage(), e);
    }
  }
}
