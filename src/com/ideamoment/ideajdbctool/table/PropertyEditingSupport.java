/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * @author Chinakite
 *
 */
public class PropertyEditingSupport extends EditingSupport {

	private final CellEditor cellEditor;
	private final IValue valueHandler;
	private final IValueFormatter valueFormatter;

	public PropertyEditingSupport(ColumnViewer viewer, String propertyName, CellEditor cellEditor) {
		this(viewer, new PropertyValue(propertyName), null, cellEditor);
	}

	public PropertyEditingSupport(ColumnViewer viewer, IValue valueHandler, IValueFormatter valueFormatter,
			CellEditor cellEditor) {
		super(viewer);
		this.valueHandler = valueHandler;
		this.valueFormatter = valueFormatter;
		this.cellEditor = cellEditor;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		try {
			Object value = valueHandler.getValue(element);
			if (valueFormatter != null) {
				value = valueFormatter.format(value);
			}
			return value;
		} catch (Exception e) {
			// Exceptions are not re-thrown because EditingSupport is touchy in
			// that regard
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void setValue(Object element, Object value) {
		try {
			Object parsedValue = value;
			if (valueFormatter != null) {
				parsedValue = valueFormatter.parse((String)value);
			}
			valueHandler.setValue(element, parsedValue);
			getViewer().refresh();
		} catch (Exception e) {
			// Exceptions are not re-thrown because EditingSupport is touchy in
			// that regard
			e.printStackTrace();
		}
	}

}
