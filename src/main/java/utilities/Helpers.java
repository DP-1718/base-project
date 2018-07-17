
package utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

public class Helpers {

	public static Object typeFromString(String type, String value) throws NumberFormatException, ParseException {
		Object realTypeValue;

		realTypeValue = null;

		if (type.equals("String")) {
			realTypeValue = value;
		} else if (type.equals("Integer")) {
			realTypeValue = new Integer(value);
		} else if (type.equals("Double")) {
			realTypeValue = new Double(value);
		} else if (type.equals("Date")) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.setLenient(false);
			realTypeValue = dateFormat.parse(value);
		} else if (type.equals("Time")) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			dateFormat.setLenient(false);
			realTypeValue = dateFormat.parse(value);
		} else if (type.equals("DateTime")) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			dateFormat.setLenient(false);
			realTypeValue = dateFormat.parse(value);
		} else if (type.equals("Boolean")) {
			value = value.toLowerCase();
			realTypeValue = new Boolean(value);
		}

		return realTypeValue;
	}

	public static Boolean validateEmail(String value) {
		Boolean isValid;

		isValid = EmailValidator.getInstance().isValid(value);

		return isValid;
	}

	public static Boolean validateUrl(String value) {
		Boolean isValid;

		isValid = UrlValidator.getInstance().isValid(value);

		return isValid;
	}

	public static Boolean min(Double value, Double compare) {
		Boolean isValid;

		isValid = value.compareTo(compare) >= 0;

		return isValid;
	}

	public static Boolean max(Double value, Double compare) {
		Boolean isValid;

		isValid = value.compareTo(compare) <= 0;

		return isValid;
	}

	public static Boolean range(Double value, Double compareMin, Double compareMax) {
		Boolean isValid;

		isValid = Helpers.min(value, compareMin) && Helpers.max(value, compareMax);

		return isValid;
	}

	public static Boolean before(Date date, Date compare) {
		Boolean isValid;

		isValid = date.before(compare);

		return isValid;
	}

	public static Boolean after(Date date, Date compare) {
		Boolean isValid;

		isValid = date.after(compare);

		return isValid;
	}

	public static Boolean between(Date date, Date compareStart, Date compareFinish) {
		Boolean isValid;

		isValid = ((Helpers.after(date, compareStart) && Helpers.before(date, compareFinish))
				|| date.equals(compareStart) || date.equals(compareFinish));

		return isValid;
	}

}
