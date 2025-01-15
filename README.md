# Tenant-Based Routing Service

This project is a Spring Boot application designed for tenant-based request routing. It dynamically handles tenant configurations, tracks metrics, and routes requests to tenant-specific endpoints.



## Features

- Dynamic tenant configurations loaded at runtime.
- Request routing based on tenant ID.
- Metrics tracking for tenant-specific requests.
- Graceful error handling with standardized responses.



## Requirements

- **Docker**: (optional, for running the app without the need for Java and Gradle)
- **Java**: 17 or higher
- **Gradle**: 7.0 or higher
- **Postman**: (optional, for testing the API)



## Setup Instructions

### 1. Build the Project

```
./gradlew build
```

### 2. Run the Application

```
./gradlew bootRun
```

### 3. Initial Data

The application comes with some initial tenant data pre-loaded for demonstration purposes. This data is added to the in-memory database (H2) at startup.

#### Preloaded Tenants:

| Tenant ID | Resource Endpoint          |
| --------- | -------------------------- |
| tenant1   | http://tenant1-service.com |
| tenant2   | http://tenant2-service.com |

To view or modify the initial data:

1. Check the `src/main/resources/data.sql` file.
2. Access the H2 console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:db`, Username: `sa`, Password: empty).

### 4. Access the Application

- The application will run on **http://localhost:8080** by default.
- Use Postman or your browser to interact with the endpoints.

## Run with Docker

To simplify the setup, a Docker image is available for the application.

### Steps:

1. **Build the Docker Image**:

   ```bash
   docker build -t tenant-routing-service .
   ```

2. **Run the Application**:

   ```
   docker run -p 8080:8080 tenant-routing-service
   ```

3. **Stop the Container**: To stop the container, press `Ctrl+C` or use the `docker stop` command:

   ```
   docker ps
   docker stop <container-id>
   ```

## API Endpoints

### **1. Route Request**

- **URL**: `/api/route`

- **Method**: `GET`

- Headers:

    - `X-Tenant-ID`: Tenant ID (e.g., `tenant1`)

- Response:

  ```json
  {
    "data": "Successfully routed"
  }
  ```

### **2. Tenant Management**

- **Add Tenant**:

    - **URL**: `/api/tenants`

    - **Method**: `POST`

    - Body:

      ```json
      {
        "tenantId": "tenant1",
        "resourceEndpoint": "http://tenant1-service.com"
      }
      ```

    - Response:

      ```json
      {
        "tenantId": "tenant1",
        "resourceEndpoint": "http://tenant1-service.com"
      }
      ```

- **Get Tenant**:

    - **URL**: `/api/tenants/{tenantId}`

    - **Method**: `GET`

    - Response:

      ```json
      {
        "tenantId": "tenant1",
        "resourceEndpoint": "http://tenant1-service.com"
      }
      ```

- **Delete Tenant**:

    - **URL**: `/api/tenants/{tenantId}`
    - **Method**: `DELETE`
    - **Response**: HTTP `204 NO_CONTENT` (empty body if successful)

### **3. Actuator Metrics**

- **URL**: `/actuator/metrics`

- **Method**: `GET`

- **Description**: Returns application metrics, including custom tenant-specific metrics.

- Filter Specific Metric:

  ```
  GET /actuator/metrics/{metric-name}
  ```



## Running Tests

To run the tests, execute:

```
./gradlew test
```



## Metrics and Monitoring

1. **Metrics Endpoint**:
    - Metrics are exposed at `/actuator/metrics`.
2. **Custom Metrics**:
    - `tenant_requests`: Counts the number of requests per tenant.
    - `tenant_request_latency`: Measures request latency for each tenant.
3. **Viewing Metrics**:
    - Access metrics via Postman:
        - URL: `http://localhost:8080/actuator/metrics`
    - Filter specific metrics:
        - URL: `http://localhost:8080/actuator/metrics/{metric-name}`



## Troubleshooting

1. **Port Conflicts**:

    - Ensure port 8080 is not in use or change the port in `application.properties`:

      ```
      server.port=8081
      ```

2. **Build Errors**:

    - Ensure you have the correct Java and Gradle versions installed.
    - Run `./gradlew --version` to verify the Gradle version.
