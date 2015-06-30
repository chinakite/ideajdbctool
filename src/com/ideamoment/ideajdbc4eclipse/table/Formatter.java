/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.table;

import java.text.DateFormat;
import java.text.NumberFormat;

import org.eclipse.core.databinding.conversion.IConverter;

/**
 * @author Chinakite
 *
 */
public class Formatter {
	/**
	 * Returns a formatter for String to double/Double by NumberFormat
	 */
	public static StringValueFormatter forDouble(NumberFormat numberFormat) {
		return new StringValueFormatter(numberFormat) {
			@Override
			public Object parse(String str) {
				return ((Number) super.parse(str)).doubleValue();
			}
		};
	}

	/**
	 * Returns a formatter for String to long/Long by NumberFormat
	 */
	public static StringValueFormatter forLong(NumberFormat numberFormat) {
		return new StringValueFormatter(numberFormat) {
			@Override
			public Object parse(String str) {
				return ((Number) super.parse(str)).longValue();
			}
		};
	}

	/**
	 * Returns a formatter for String to float/Float by NumberFormat
	 */
	public static StringValueFormatter forFloat(NumberFormat numberFormat) {
		return new StringValueFormatter(numberFormat) {
			@Override
			public Object parse(String str) {
				return ((Number) super.parse(str)).floatValue();
			}
		};
	}

	/**
	 * Returns a formatter for String to int/Integer by NumberFormat
	 */
	public static StringValueFormatter forInt(NumberFormat numberFormat) {
		numberFormat.setParseIntegerOnly(true);
		return new StringValueFormatter(numberFormat);
	}

	/**
	 * Returns a formatter for String to Date by DateFormat.
	 */
	public static StringValueFormatter forDate(DateFormat dateFormat) {
		return new StringValueFormatter(dateFormat);
	}

	/**
	 * Returns a value formatter by two existing data binding IConverterts, on
	 * for each direction.
	 */
	public static IValueFormatter fromConverters(final IConverter format, final IConverter parse) {
		return new IValueFormatter() {

			public String format(Object obj) {
				return (String)format.convert(obj);
			}

			public Object parse(String obj) {
				return parse.convert(obj);
			}
		};
	}

	/**
	 * Returns a formatter for string to int/Integer.
	 */
	public static IValueFormatter forInt() {
		return new IValueFormatter() {

			public String format(Object obj) {
				return Integer.toString((Integer)obj);
			}

			public Object parse(String obj) {
				return Integer.parseInt(obj);
			}

		};
	}
}
