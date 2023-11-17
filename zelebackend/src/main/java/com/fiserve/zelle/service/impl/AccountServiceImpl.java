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
import java.util.Optional;

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
	public String createAccount(EmployeeDetail accountSchema) throws Exception {

		String acctResponse;

		try {
			EmployeeDetail schema = repository.save(accountSchema);
			acctResponse = "account created sucessfulliy with employeeId : " + schema.getEmployeeId();

		} catch (Exception e) {
			throw e;
		}

		return acctResponse;

	}

	@Override
	public AccountResponse deductTax(Long employeeId) throws Exception {
		AccountResponse accountResponse = new AccountResponse();
		Optional<EmployeeDetail> account = repository.findById(employeeId);
		if (account.isEmpty()) {
			throw new InvalidDataException("Invalid employee_id/employee_id not present please sign up");
		}
//		EmployeeDetail accountSchema = repository.getById(employeeId);
		EmployeeDetail accountSchema = account.get();
		double tax_amount = 0.0;
		String taxResponse;
		double tempSalary = 0.0;
		double tempTax = 0;
//		double salary = repository.getSalaryByEmployeeId(employeeId);
		double salary = accountSchema.getSalary();
		double cess = 0;
		double netSalary;
		double lossOfPay = salary / 30;
		LocalDate financialEndingDate = LocalDate.of(Year.now().getValue(), Month.MARCH, 31);
		LocalDate joining = accountSchema.getDoj().toLocalDate();
//		LocalDate joining = doj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		LocalDate joining = repository.getDojByEmployeeId(employeeId).toLocalDate();
		Period age = Period.between(joining, financialEndingDate);
		int months = age.getMonths();
		int days = age.getDays() ;
		netSalary = salary * months + lossOfPay * days;

		if (netSalary <= 250000) {
			tax_amount = 0.0;
		} else if ((netSalary > 250000) && (netSalary <= 500000)) {
			tempSalary = netSalary - 250000;

			tax_amount = 0.05 * tempSalary;
		} else if ((netSalary > 500000) && (netSalary <= 1000000)) {
			tempSalary = netSalary - 500000;
			tempTax = 0.10 * tempSalary;
			tax_amount = tempTax + 0.05 * 250000;
		} else if ((netSalary > 1000000) && (netSalary <= 2500000)) {
			tempSalary = netSalary - 1000000;
			tempTax = 0.20 * tempSalary + 0.10 * 500000 + 0.05 * 250000;
			tax_amount = tempTax;
		} else if ((netSalary > 2500000)) {
			tempSalary = netSalary - 1000000;
			cess = 0.02 * (netSalary - 2500000);
			tempTax = 0.20 * tempSalary + 0.10 * 500000 + 0.05 * 250000 + cess;
			tax_amount = tempTax;

		}
		accountResponse.setEmployeeId(accountSchema.getEmployeeId());
		accountResponse.setFirstName(accountSchema.getFirstName());
		accountResponse.setLastName(accountSchema.getLastName());
		accountResponse.setYearlySalary(netSalary);
		accountResponse.setTaxAmount(tax_amount);
		accountResponse.setCessAmount(cess);
		/*
		 * taxResponse = "The tax to be deducted from the salary is: " + tax_amount +
		 * "Salary after tax deduction is: " + (salary - tax_amount);
		 */
		return accountResponse;
	}

	@Override
	public void validateEmployeeDetail(EmployeeDetail empDetail) throws Exception {

		if (empDetail.getEmployeeId() == null) {
			throw new InvalidDataException("invallid employee_id");
		}

		if (empDetail.getFirstName() == null || empDetail.getFirstName().isBlank()) {
			throw new InvalidDataException("invallid firstName");
		}

		if (empDetail.getLastName() == null || empDetail.getLastName().isBlank()) {
			throw new InvalidDataException("invallid lastName");
		}

		if (!DataValidator.isValidEmail(empDetail.getEmail())) {
			throw new InvalidDataException("invallid email adress");
		}

		if (!DataValidator.isValidPhoneNumber(empDetail.getPhoneNumber())) {
			throw new InvalidDataException("invallid phone number");
		}

		if (!DataValidator.isValidDate(empDetail.getDoj())) {
			throw new InvalidDataException("invallid date of joining");
		}
		if (!DataValidator.isValidSalary(empDetail.getSalary())) {
			throw new InvalidDataException("invallid salary");
		}

	}
}
