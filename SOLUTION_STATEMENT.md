## **Solution Statement**

### **Problem Interpretation and Scope**

The task was to create a multi-tenant web service using Spring Boot that routes tenant-based requests to specific resource endpoints. Each tenant's configuration must be handled dynamically without requiring an application restart. Additionally, the service needed to include monitoring, logging, error handling, and scalability considerations.

### **Key Design and Architectural Decisions**

#### **1. Dynamic Tenant Configuration**

- An API was implemented to **add, retrieve, and remove tenant configurations dynamically** at runtime. This ensures flexibility for adding new tenants or modifying existing configurations without restarting the application.
- **Spring Data JPA** was used for tenant configuration management. JPA provides an abstraction layer for working with the database, allowing easy querying and persistence of tenant data.
- Tenant configurations are stored in an **in-memory database** (H2) for simplicity. This allowed rapid prototyping and testing.

#### **2. Request Routing**

- **Spring’s RestTemplate** was used for request routing to the tenant-specific resource endpoints. RestTemplate was chosen for simplicity, as it offers a straightforward way to handle HTTP requests in a blocking manner.
- Each tenant is identified via the `X-Tenant-ID` HTTP header. This header ensures clear segregation of requests and tenant-specific handling.

#### **3. Monitoring and Logging**

- Added tenant-specific metrics using Micrometer and Spring Boot Actuator. Key metrics include:
    - The total number of requests per tenant.
    - Latency of requests per tenant.
- These metrics can be exposed via `/actuator/metrics` and integrated with **Prometheus** for monitoring and visualization.
- Logging was implemented to capture tenant-specific events, errors, and application flow for debugging and analysis.

#### **4. Error Handling**

- Implemented global exception handling using `@RestControllerAdvice` to gracefully handle:
    - Missing tenants (`TenantNotFoundException`).
    - Invalid configurations or requests (`RequestProcessingException`).
    - Missing headers (`MissingHeaderException`).
- Standardized error responses were added, including details such as the error message, timestamp, and cause.

#### **5. Testing**

- Comprehensive unit tests were written for key components:
    - Controllers, services, and metrics logic.
    - Exception handling and routing logic.
- Tests ensured the correctness of API functionality and adherence to requirements.

------

### **Scalability and Performance**

#### Current Design:

- The service is designed for simplicity, using synchronous request handling (`RestTemplate`) and in-memory configurations.

#### Future Improvements:

1. **Spring Cloud Gateway**:
    - Use Spring Cloud Gateway for **non-blocking routing** and advanced features like rate-limiting and request filtering.
    - This would improve performance under high load and simplify routing logic.
2. **Reactive Programming**:
    - Replace `RestTemplate` with **WebClient** for non-blocking I/O. Reactive programming would ensure better scalability and resource utilization.

------

### **Production-Ready Considerations**

1. **Authentication and Authorization**:
    - Implement tenant-specific authentication and authorization:
        - **Custom Implementation**: Validate tokens or API keys using an in-house solution.
        - **External Solutions**: Integrate with an Identity Provider like **Keycloak** for OAuth2 or OpenID Connect support.
2. **Fault Tolerance and Circuit Breakers**:
    - Use a library like Resilience4j or Spring Cloud Circuit Breaker to:
        - Handle failures when communicating with tenant-specific endpoints.
        - Implement retry mechanisms, fallback responses, and rate limiting.
3. **Persistence**:
    - Replace the in-memory database (H2) with **PostgreSQL** or another persistent database.
    - Use Spring Data JPA for managing tenant configurations in the external database.
4. **Per-Tenant Configurations**:
    - Store tenant-specific configurations (e.g., rate limits, timeouts, access policies) in the database.
    - Dynamically load these configurations for each request.
5. **Deployment**:
    - Containerize the application using Docker to simplify deployment.
    - Use Kubernetes for orchestration and scaling in production environments.

------

### **Trade-Offs**

1. **Blocking vs Non-Blocking**:
    - RestTemplate (blocking) was chosen for simplicity but may not scale efficiently for high-concurrency scenarios. Reactive programming would be more suitable for such cases.
2. **In-Memory Database**:
    - H2 was used to simplify the setup and testing. In production, a robust database like PostgreSQL would be necessary.
3. **Custom Metrics vs Out-of-the-Box Tools**:
    - Custom metrics and logging provide flexibility but require additional development and maintenance compared to fully managed solutions.

------

### **Conclusion**

The solution was designed to meet the core requirements while maintaining simplicity and extensibility. It includes:

- APIs for managing tenant configurations dynamically.
- A routing mechanism based on tenant-specific configurations.
- Metrics and logging for monitoring tenant activity.
- Robust error handling for missing tenants, invalid requests, and other scenarios.

The current implementation is suitable for small to medium-scale systems. For large-scale, production-ready deployments, enhancements like Spring Cloud Gateway, reactive programming, external authentication, fault tolerance, and persistent storage would be necessary.
