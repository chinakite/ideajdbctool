/**
 * 
 */
package com.ideamoment.ideajdbctool.editors;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.ideamoment.ideajdbctool.IdeaJdbcActivator;
import com.ideamoment.ideajdbctool.database.Col;
import com.ideamoment.ideajdbctool.database.DataBaseUtils;
import com.ideamoment.ideajdbctool.table.ColumnBuilder;
import com.ideamoment.ideajdbctool.table.TableViewerBuilder;

/**
 * @author Chinakite
 * 
 */
public class ClauseEditor extends EditorPart {

	private Text selectClauseText;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL));

		Composite tableBox = new Composite(composite, SWT.BORDER | SWT.MULTI);
		tableBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1,
				1));

		selectClauseText = new Text(composite, SWT.BORDER | SWT.MULTI);
		selectClauseText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));
		
		
		createTableViewer(tableBox);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/**
	 * Create the TableViewer
	 */
	private void createTableViewer(Composite tableBox) {

		final TableViewerBuilder t = new TableViewerBuilder(tableBox, SWT.FULL_SELECTION);

		final TableViewer tver = t.getTableViewer();
		ColumnViewerEditorActivationStrategy activationStrategy=new ColumnViewerEditorActivationStrategy(tver);
		int feature=ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR;
		
		TableViewerEditor.create(tver, activationStrategy, feature);

		ColumnBuilder lineNumCol = t.createColumn(" ");
		lineNumCol.setPixelWidth(50);
		lineNumCol.setCustomLabelProvider(new ColumnLabelProvider() { 
			public void update(ViewerCell cell) { 
				cell.setText(t.getTable().indexOf((TableItem)cell.getItem())+"") ;
			} 
		});
		lineNumCol.build();
		
		ColumnBuilder checkCol = t.createColumn(" ");
		checkCol.setPixelWidth(30);
		checkCol.bindToProperty("checked");
		checkCol.setCustomLabelProvider(new ColumnLabelProvider() { 
			public Image getImage(Object obj) {
				if(obj instanceof Col) {
					if(((Col)obj).isChecked()) {
						ImageDescriptor imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(IdeaJdbcActivator.PLUGIN_ID, "icons/checkbox_yes.png");
						return imageDesc.createImage();
					}else{
						ImageDescriptor imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(IdeaJdbcActivator.PLUGIN_ID, "icons/checkbox_no.png");
						return imageDesc.createImage();
					}
				}else{
					return null;
				}
			}
		});
		CheckboxCellEditor checkBoxEditor = new CheckboxCellEditor(t.getTable(), SWT.CHECK);
		checkCol.makeEditable(checkBoxEditor);
		checkCol.build();

		ColumnBuilder nameCol = t.createColumn("字段名称");
		nameCol.bindToProperty("name");
		nameCol.setPercentWidth(50);
		nameCol.build();

		ColumnBuilder aliasCol = t.createColumn("别名");
		aliasCol.setPercentWidth(50);
		aliasCol.bindToProperty("name");
		aliasCol.makeEditable();
		aliasCol.build();

		ClauseEditorInput eInput = (ClauseEditorInput) this.getEditorInput();
		final String tblName = eInput.getName();

		List<Col> cols = DataBaseUtils
				.getAllCols(eInput.getConnInfo(), tblName);

		String s = getSelectClauseStr(tblName, cols);
		
		final Table table = t.getTable();
		table.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = table.getItem(pt);
				if (item == null)
					return;
				Rectangle rect = item.getBounds(1);
				if (rect.contains(pt)) {
					List curCols = (List)tver.getInput();
					String s = getSelectClauseStr(tblName, curCols);
					selectClauseText.setText(s);
				}
			}
		});
		
		t.setInput(cols);
		
		selectClauseText.setText(s);
	}

	private String getSelectClauseStr(String tblName, List<Col> cols) {
		StringBuffer sb = new StringBuffer("SELECT ");
		int i = 0;
		for(Col col : cols) {
			if(col.isChecked()) {
				if(i > 0) {
					sb.append(", ");
				}
				sb.append(col.getName());
				i++;
			}
		}
		sb.append(" FROM ").append(tblName.toUpperCase());
		return sb.toString();
	}

}
