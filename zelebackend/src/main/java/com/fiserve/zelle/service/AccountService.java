/**
 * 
 */
package com.fiserve.zelle.service;

import com.fiserve.zelle.model.EmployeeDetail;
import com.fiserve.zelle.response.AccountResponse;

/**
 * @author 
 *
 */
public interface AccountService {

	public String createAccount(EmployeeDetail accountSchema) throws Exception;
	
	public AccountResponse deductTax(Long employee_id) throws Exception;
	
	public void  validateEmployeeDetail(EmployeeDetail empDetail) throws Exception;
}
