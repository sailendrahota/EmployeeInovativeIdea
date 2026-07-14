package com.fiserve.zelle.controller;

import com.fiserve.zelle.exception.ApiException;
import com.fiserve.zelle.exception.InvalidDataException;
import com.fiserve.zelle.model.EmployeeDetail;
import com.fiserve.zelle.response.AccountResponse;
import com.fiserve.zelle.service.AccountService;
import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController


@CrossOrigin(origins = "*")
public class EmployeeController {

	@Autowired
	private AccountService accountService;

	/**
	 * Account Creation
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/registeremployee")
	/*@ApiOperation(value = "Create new Employee")
	@ApiResponses({
			@ApiResponse(code = 201, message = "Account created successfully", response = AccountResponse.class),
			@ApiResponse(code = 401, message = "Unauthorised Account"),
			@ApiResponse(code = 403, message = "Forbidden Account"),
			@ApiResponse(code = 404, message = "Account already exist"),
			@ApiResponse(code = 500, message = "Internal error") })*/
	public ResponseEntity registerUser(@Validated @RequestBody EmployeeDetail employeeDetails, HttpHeaders headers) {
		try {
			accountService.validateEmployeeDetail(employeeDetails);
			String data = accountService.createAccount(employeeDetails);
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		} catch (Exception e) {
			if (e instanceof ApiException) {
				return new ResponseEntity(((ApiException) e).body, ((ApiException) e).code);
			}
			if (e instanceof InvalidDataException) {
				throw new InvalidDataException(e.getMessage());
			} else {
				e.printStackTrace();
				return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/tax")
	/*@ApiOperation(value = "calculate and process tax")
	@ApiResponses({ @ApiResponse(code = 201, message = "tax deduction sucessful", response = String.class),
			@ApiResponse(code = 401, message = "Unauthorised Account"),
			@ApiResponse(code = 403, message = "Forbidden Account"),
			@ApiResponse(code = 500, message = "Internal error") })*/
	public ResponseEntity taxDetail(@Validated @RequestParam("employee_id") Long id, HttpHeaders headers) {
		try {

	//		accountService.validateEmployeeDetail(employeeDetails);
			if(id ==null) {
				throw new InvalidDataException("Invalid employee_id");
			}
			AccountResponse data = accountService.deductTax(id);
			return new ResponseEntity<AccountResponse>(data, HttpStatus.OK);
		} catch (Exception e) {
			if (e instanceof ApiException) {
				return new ResponseEntity(((ApiException) e).body, ((ApiException) e).code);
			}
			if (e instanceof InvalidDataException) {
				throw new InvalidDataException(e.getMessage());
			} else {
				e.printStackTrace();
				return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}
	@GetMapping("/info")
	public Map<String, String> getEnvironmentDetails() {
		Map<String, String> metadata = new LinkedHashMap<>();

		// 1. Fetching internal JVM & OS properties using System.getProperty
		metadata.put("java_version", System.getProperty("java.version"));
		metadata.put("java_vendor", System.getProperty("java.vendor"));
		metadata.put("os_name", System.getProperty("os.name"));
		metadata.put("os_architecture", System.getProperty("os.arch"));

		// 2. Fetching runtime environment injected by Kubernetes using System.getenv
		// If the variable doesn't exist, it gracefully defaults to "standalone-local"
		String currentEnv = System.getenv().getOrDefault("APP_ENV", "standalone-local");
		metadata.put("runtime_environment", currentEnv);

		return metadata;
	}

}
