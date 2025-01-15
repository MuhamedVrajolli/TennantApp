package com.softco.tenantapp.routing;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.softco.tenantapp.exception.RequestProcessingException;
import com.softco.tenantapp.exception.TenantNotFoundException;
import com.softco.tenantapp.model.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoutingController.class)
class RoutingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private RoutingService routingService;

  @Test
  void testRouteRequest_success() throws Exception {
    // Arrange
    String tenantId = "tenant1";
    String responseBody = "Successfully routed";
    ApiResponse<String> apiResponse = new ApiResponse<>(responseBody);

    when(routingService.routeRequest(tenantId)).thenReturn(apiResponse);

    // Act & Assert
    mockMvc.perform(get("/api/route")
            .header("X-Tenant-ID", tenantId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data").value(responseBody));
  }

  @Test
  void testRouteRequest_MissingHeader() throws Exception {
    // Act & Assert
    mockMvc.perform(get("/api/route")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Missing required header: X-Tenant-ID"));
  }

  @Test
  void testRouteRequest_tenantNotFound() throws Exception {
    // Arrange
    String tenantId = "nonexistent";
    when(routingService.routeRequest(tenantId)).thenThrow(new TenantNotFoundException("Tenant with ID " + tenantId + " not found"));

    // Act & Assert
    mockMvc.perform(get("/api/route")
            .header("X-Tenant-ID", tenantId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Tenant with ID " + tenantId + " not found"));
  }

  @Test
  void testRouteRequest_requestFailure() throws Exception {
    // Arrange
    String tenantId = "tenant1";
    when(routingService.routeRequest(tenantId)).thenThrow(new RequestProcessingException("Routing failed for tenant ID " + tenantId));

    // Act & Assert
    mockMvc.perform(get("/api/route")
            .header("X-Tenant-ID", tenantId)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Routing failed for tenant ID " + tenantId));
  }
}

