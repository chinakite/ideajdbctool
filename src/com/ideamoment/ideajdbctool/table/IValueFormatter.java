/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

/**
 * @author Chinakite
 *
 */
public interface IValueFormatter {

	public String format(Object obj);

	public Object parse(String obj);

}
