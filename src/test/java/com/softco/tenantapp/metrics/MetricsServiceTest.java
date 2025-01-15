package com.softco.tenantapp.metrics;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MetricsServiceTest {

  @Mock
  private MeterRegistry meterRegistry;

  @Mock
  private Counter counter;

  @Mock
  private Timer timer;

  @InjectMocks
  private MetricsService metricsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIncrementTenantRequestCount() {
    // Arrange
    String tenantId = "tenant1";
    when(meterRegistry.counter("tenant.requests.count", "tenantId", tenantId)).thenReturn(counter);

    // Act
    metricsService.incrementTenantRequestCount(tenantId);

    // Assert
    verify(meterRegistry, times(1)).counter("tenant.requests.count", "tenantId", tenantId);
    verify(counter, times(1)).increment();
  }

  @Test
  void testRecordTenantRequestLatency() {
    // Arrange
    String tenantId = "tenant1";
    double latency = 150.0;
    when(meterRegistry.timer("tenant.requests.latency", "tenantId", tenantId)).thenReturn(timer);

    // Act
    metricsService.recordTenantRequestLatency(tenantId, latency);

    // Assert
    verify(meterRegistry, times(1)).timer("tenant.requests.latency", "tenantId", tenantId);
    verify(timer, times(1)).record((long) latency, java.util.concurrent.TimeUnit.MILLISECONDS);
  }
}
