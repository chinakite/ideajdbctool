/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Chinakite
 *
 */
public class ColumnSortSelectionListener extends SelectionAdapter {
	private final TableViewer viewer;

	public ColumnSortSelectionListener(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		TableColumn column = (TableColumn) e.getSource();
		Table table = column.getParent();
		boolean alreadyActiveSortColumn = (column == table.getSortColumn());
		if (alreadyActiveSortColumn) {
			table.setSortDirection(table.getSortDirection() == SWT.DOWN ? SWT.UP : SWT.DOWN);
		} else {
			table.setSortColumn(column);
			table.setSortDirection(SWT.UP);
		}
		viewer.refresh();
	}
}
