/**
 * 
 */
package com.fiserve.zelle.service;

import com.fiserve.zelle.model.EmployeeDetail;
import com.fiserve.zelle.response.AccountResponse;

/**
 * @author syedmohamedyusuf
 *
 */
public interface AccountService {

	public AccountResponse createAccount(EmployeeDetail accountSchema) throws Exception;
	
	public String deductTax(EmployeeDetail accountSchema) throws Exception;
}
