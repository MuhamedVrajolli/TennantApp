package com.softco.tenantapp.tenantdata;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softco.tenantapp.exception.TenantNotFoundException;
import com.softco.tenantapp.model.TenantDataModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TenantDataController.class)
class TenantDataControllerTest {

  private static final String PATH = "/api/tenants" ;
  private static final String TENANT_ID = "tenant1";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TenantDataService tenantDataService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testGetTenantById() throws Exception {
    // Arrange
    TenantDataModel tenantData = new TenantDataModel(TENANT_ID, "https://tenant1-service.com");
    when(tenantDataService.getTenantById(TENANT_ID)).thenReturn(tenantData);

    // Act & Assert
    mockMvc.perform(get(PATH + "/" + TENANT_ID))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tenantId").value(TENANT_ID))
        .andExpect(jsonPath("$.resourceEndpoint").value("https://tenant1-service.com"));
  }

  @Test
  void testGetTenantById_error() throws Exception {
    // Arrange
    when(tenantDataService.getTenantById(TENANT_ID)).thenThrow(new TenantNotFoundException("Tenant not found"));

    // Act & Assert
    mockMvc.perform(get(PATH + "/" + TENANT_ID))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Tenant not found"))
        .andExpect(jsonPath("$.date").exists())
        .andExpect(jsonPath("$.cause").exists());
  }

  @Test
  void testAddTenant() throws Exception {
    // Arrange
    TenantDataModel tenantData = new TenantDataModel(TENANT_ID, "https://tenant1-service.com");
    when(tenantDataService.addTenant(any(TenantDataModel.class))).thenReturn(tenantData);

    // Act & Assert
    mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tenantData)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.tenantId").value(TENANT_ID))
        .andExpect(jsonPath("$.resourceEndpoint").value("https://tenant1-service.com"));
  }

  @Test
  void testRemoveTenant() throws Exception {
    // Arrange
    doNothing().when(tenantDataService).removeTenant(TENANT_ID);

    // Act & Assert
    mockMvc.perform(delete(PATH + "/" + TENANT_ID))
        .andExpect(status().isNoContent());
  }
}
