package com.fiserve.zelle.controller;

import com.fiserve.zelle.exception.ApiException;
import com.fiserve.zelle.exception.InvalidDataException;
import com.fiserve.zelle.model.EmployeeDetail;
import com.fiserve.zelle.response.AccountResponse;
import com.fiserve.zelle.service.AccountService;

// Modern OpenAPI 3 imports for Spring Boot 3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.extern.slf4j.Slf4j; // Lombok Logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j // Injects the 'log' variable automatically
@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private AccountService accountService;

    /**
     * Account Creation
     */
    @PostMapping("/registeremployee")
    @Operation(summary = "Create new Employee") // Upgraded from @ApiOperation
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorised Account"),
            @ApiResponse(responseCode = "403", description = "Forbidden Account"),
            @ApiResponse(responseCode = "404", description = "Account already exist"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Object> registerUser(@RequestBody EmployeeDetail employeeDetails) {
        log.info("Request received to register employee with email: {}", employeeDetails.getEmail());

        try {
            accountService.validateEmployeeDetail(employeeDetails);
            String data = accountService.createAccount(employeeDetails);

            log.info("Successfully created account for: {}", employeeDetails.getEmail());
            return new ResponseEntity<>(data, HttpStatus.CREATED);

        } catch (Exception e) {
            if (e instanceof ApiException apiException) {
                log.error("API Exception during registration: {}", apiException.getMessage());
                return new ResponseEntity<>(apiException.body, apiException.code);
            }
            if (e instanceof InvalidDataException invalidDataException) {
                log.warn("Validation failed: {}", invalidDataException.getMessage());
                throw invalidDataException;
            } else {
                // Replaces e.printStackTrace() with a proper error log containing the stack trace
                log.error("Unexpected error during employee registration", e);
                return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Tax Calculation
     */
    @GetMapping("/tax")
    @Operation(summary = "Calculate and process tax")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tax deduction successful", content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorised Account"),
            @ApiResponse(responseCode = "403", description = "Forbidden Account"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Object> taxDetail(@RequestParam("employee_id") Long id) {
        log.info("Request received to calculate tax for employee_id: {}", id);

        try {
            if (id == null) {
                log.warn("Tax calculation failed: employee_id is null");
                throw new InvalidDataException("Invalid employee_id");
            }
            AccountResponse data = accountService.deductTax(id);

            log.info("Successfully calculated tax for employee_id: {}", id);
            return new ResponseEntity<>(data, HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof ApiException apiException) {
                log.error("API Exception during tax calculation: {}", apiException.getMessage());
                return new ResponseEntity<>(apiException.body, apiException.code);
            }
            if (e instanceof InvalidDataException invalidDataException) {
                log.warn("Validation failed for tax calculation: {}", invalidDataException.getMessage());
                throw invalidDataException;
            } else {
                log.error("Unexpected error during tax calculation for employee_id: {}", id, e);
                return new ResponseEntity<>("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Info Endpoint
     */
    @GetMapping("/info")
    @Operation(summary = "Get Environment Details")
    public Map<String, String> getEnvironmentDetails() {
        log.debug("Fetching environment details");
        Map<String, String> metadata = new LinkedHashMap<>();

        // 1. Fetching internal JVM & OS properties using System.getProperty
        metadata.put("java_version", System.getProperty("java.version"));
        metadata.put("java_vendor", System.getProperty("java.vendor"));
        metadata.put("os_name", System.getProperty("os.name"));
        metadata.put("os_architecture", System.getProperty("os.arch"));

        // 2. Fetching runtime environment injected by Kubernetes using System.getenv
        String currentEnv = System.getenv().getOrDefault("APP_ENV", "standalone-local");
        metadata.put("runtime_environment", currentEnv);

        return metadata;
    }
}