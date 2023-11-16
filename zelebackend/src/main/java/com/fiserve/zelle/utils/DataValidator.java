package com.fiserve.zelle.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		String salaryRegex = "^\\d*\\.\\d+|\\d+\\.\\d*$";

		Pattern pat = Pattern.compile(salaryRegex);
		if (salary == null)
			return false;
		return pat.matcher(sal).matches();

	}

	public static boolean isValidDate(Date date) {
		
		String dateStr= date.toString();
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    
		/*DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
		if (dateStr == null)
			return false;
		try {
			LocalDate.parse(dateStr, dateFormatter);
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;*/
	}

}
