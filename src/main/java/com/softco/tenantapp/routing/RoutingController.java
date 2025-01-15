package com.softco.tenantapp.routing;

import com.softco.tenantapp.exception.MissingHeaderException;
import com.softco.tenantapp.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class RoutingController {

  private final RoutingService routingService;

  /**
   * Routes a GET request to the tenant-specific endpoint.
   *
   * @param tenantId the unique ID of the tenant (provided in the "X-Tenant-ID" header).
   * @return an {@link ApiResponse} containing the response body as a string.
   * @throws MissingHeaderException if the "X-Tenant-ID" header is missing.
   */
  @GetMapping("/route")
  public ApiResponse<String> routeRequest(@RequestHeader(value = "X-Tenant-ID", required = false) String tenantId) {
    if (tenantId == null || tenantId.isEmpty()) {
      throw new MissingHeaderException("Missing required header: X-Tenant-ID");
    }
    return routingService.routeRequest(tenantId);
  }
}
