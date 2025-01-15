package com.softco.tenantapp.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {

  private final MeterRegistry meterRegistry;

  /**
   * Increments a request count metric for a specific tenant.
   *
   * @param tenantId the ID of the tenant
   */
  public void incrementTenantRequestCount(String tenantId) {
    meterRegistry.counter("tenant.requests.count", "tenantId", tenantId).increment();
  }

  /**
   * Records request latency for a specific tenant.
   *
   * @param tenantId the ID of the tenant
   * @param latency the time taken to process the request in milliseconds
   */
  public void recordTenantRequestLatency(String tenantId, double latency) {
    meterRegistry.timer("tenant.requests.latency", "tenantId", tenantId)
        .record((long) latency, java.util.concurrent.TimeUnit.MILLISECONDS);
  }
}
