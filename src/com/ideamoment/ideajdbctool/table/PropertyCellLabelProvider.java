/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * @author Chinakite
 *
 */
public class PropertyCellLabelProvider extends CellLabelProvider {

	private final IValue valueHandler;
	private IValueFormatter valueFormatter;
	private final ICellFormatter cellFormatter;

	public PropertyCellLabelProvider(String propertyName) {
		this.valueHandler = new PropertyValue(propertyName);
		this.cellFormatter = null;
	}

	public PropertyCellLabelProvider(IValue valueHandler, IValueFormatter valueFormatter, ICellFormatter cellFormatter) {
		this.valueHandler = valueHandler;
		this.valueFormatter = valueFormatter;
		this.cellFormatter = cellFormatter;
	}

	@Override
	public void update(ViewerCell cell) {
		try {
			Object rawValue = null;
			if (valueHandler != null) {
				rawValue = valueHandler.getValue(cell.getElement());
				Object formattedValue = rawValue;
				if (valueFormatter != null) {
					formattedValue = valueFormatter.format(rawValue);
				}
				cell.setText(String.valueOf(formattedValue));
			}
			if (cellFormatter != null) {
				cellFormatter.formatCell(cell, rawValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
