/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.table;

/**
 * @author Chinakite
 *
 */
public interface IValueFormatter {

	public String format(Object obj);

	public Object parse(String obj);

}
