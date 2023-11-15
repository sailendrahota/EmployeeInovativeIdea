package com.fiserve.zelle.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class DataValidator {

	public static boolean isValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();

	}

	public static boolean isValidPhoneNumber(String phnNumber) {
		String phoneNumberRegex = "^\\d{10}$";

		Pattern pat = Pattern.compile(phoneNumberRegex);
		if (phnNumber == null)
			return false;
		return pat.matcher(phnNumber).matches();

	}

	public static boolean isValidSalary(Double salary) {
		
		String sal= Double.toString(salary);
		String salaryRegex = "(?!0+(?:\\\\.0+)?$)[0-9]+(?:\\\\.[0-9]+)?";

		Pattern pat = Pattern.compile(salaryRegex);
		if (salary == null)
			return false;
		return pat.matcher(sal).matches();

	}

	public static boolean isValidDate(Date date) {
		
		String dateStr= date.toString();
		DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
		if (dateStr == null)
			return false;
		try {
			LocalDate.parse(dateStr, dateFormatter);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}

}
