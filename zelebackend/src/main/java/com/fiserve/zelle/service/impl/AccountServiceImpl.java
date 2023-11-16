/**
 * 
 */
package com.fiserve.zelle.service.impl;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiserve.zelle.exception.InvalidDataException;
import com.fiserve.zelle.model.EmployeeDetail;
import com.fiserve.zelle.repository.AccountRepository;
import com.fiserve.zelle.response.AccountResponse;
import com.fiserve.zelle.service.AccountService;
import com.fiserve.zelle.utils.DataValidator;

/**
 * @author
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repository;

	/* Create new Account */
	@Override
	public AccountResponse createAccount(EmployeeDetail accountSchema) throws Exception {
		
		

		AccountResponse accountResponse = new AccountResponse();

		try {
			EmployeeDetail schema = repository.save(accountSchema);
			accountResponse.setEmployeeId(schema.getEmployeeId());
			accountResponse.setFirstName(schema.getFirstName());
			accountResponse.setLastName(schema.getLastName());
			accountResponse.setEmail(schema.getEmail());
			accountResponse.setPhoneNumber(schema.getPhoneNumber());
			accountResponse.setDateOfJoining(schema.getDoj());
			accountResponse.setSalary(schema.getSalary());

		} catch (Exception e) {
			return accountResponse;
		}

		return accountResponse;

	}

	@Override
	public String deductTax(EmployeeDetail accountSchema) throws Exception {

		double tax_amount = 0.0;
		String taxResponse;
		double tempSalary = 0.0;
		double tempTax = 0;
		double salary = accountSchema.getSalary();
		double netSalary;
        double lossOfPay=salary/30;
		LocalDate financialEndingDate = LocalDate.of(Year.now().getValue(), Month.MARCH, 1);
		LocalDate joining = accountSchema.getDoj().toLocalDate();
		Period age = Period.between(joining, financialEndingDate);
		int months = age.getMonths();
		int days = age.getDays();
		netSalary = salary * months + lossOfPay* days;

		if (netSalary <= 250000) {
			tax_amount = 0.0;
		} else if ((netSalary > 250000) && (salary <= 500000)) {
			tempSalary = salary - 250000;

			tax_amount = 0.05 * tempSalary;
		} else if ((netSalary > 500000) && (salary <= 1000000)) {
			tempSalary = salary - 500000;
			tempTax = 0.10 * tempSalary;
			tax_amount = tempTax + 0.05 * 250000;
		} else if ((netSalary > 1000000) && (salary <= 2500000)) {
			tempSalary = salary - 1000000;
			tempTax = 0.20 * tempSalary + 0.10 * 500000 + 0.05 * 250000;
			tax_amount = tempTax;
		} else if ((netSalary > 2500000)) {
			tempSalary = salary - 1000000;
			double cess=0.02*salary-2500000;
			tempTax = 0.20 * tempSalary + 0.10 * 500000 + 0.05 * 250000+cess;
			tax_amount = tempTax;
			
		}

		taxResponse = "The tax to be deducted from the salary is: "  + tax_amount
				+ "Salary after tax deduction is: " + (salary - tax_amount);

		return taxResponse;
	}

	@Override
	public void validateEmployeeDetail(EmployeeDetail empDetail) throws Exception {
		
		if( empDetail.getEmployeeId()== null) {
			throw  new InvalidDataException("invallid employee_id");
		}
		
		if( empDetail.getFirstName()== null) {
			throw  new InvalidDataException("invallid firstName");
		}
		
		if( empDetail.getLastName()== null) {
			throw  new InvalidDataException("invallid lastName");
		}
		
		
		if(!DataValidator.isValidEmail(empDetail.getEmail())) {
			throw  new InvalidDataException("invallid email adress");
		}
		
		if(!DataValidator.isValidPhoneNumber(empDetail.getPhoneNumber())) {
			throw  new InvalidDataException("invallid phone number");
		}
		
		if(!DataValidator.isValidDate(empDetail.getDoj())) {
			throw  new InvalidDataException("invallid date of joining");
		}
		if(!DataValidator.isValidSalary(empDetail.getSalary())) {
			throw  new InvalidDataException("invallid salary");
		}
		
		
		
		
		
		
		
		
		
		
	}
}
