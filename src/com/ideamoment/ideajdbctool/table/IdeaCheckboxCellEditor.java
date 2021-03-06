/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Chinakite
 * 
 */
public class IdeaCheckboxCellEditor extends CellEditor {

	/**
	 * The button control; initially <code>null</code>.
	 */
	protected Button button;

	/**
	 * The selection listener.
	 */
	private SelectionListener selectionListener;

	/**
	 * Default CheckboxCellEditor style.
	 */
	private static final int defaultStyle = SWT.CHECK;

	/**
	 * Creates a new checkbox cell editor with no control
	 */
	public IdeaCheckboxCellEditor() {
		setStyle(defaultStyle);
		setValueValid(true);
	}

	/**
	 * Creates a new checkbox cell editor parented under the given control. The
	 * cell editor value is a boolean value, which is initially
	 * <code>false</code>. Initially, the cell editor has no cell validator.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public IdeaCheckboxCellEditor(final Composite parent) {
		this(parent, defaultStyle);
	}

	/**
	 * Creates a new checkbox cell editor parented under the given control. The
	 * cell editor value is a boolean value, which is initially
	 * <code>false</code>. Initially, the cell editor has no cell validator.
	 * 
	 * @param parent
	 *            The parent control.
	 * @param style
	 *            The style bits.
	 */
	public IdeaCheckboxCellEditor(final Composite parent, final int style) {
		super(parent, style);
		setValueValid(true);
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(final Composite parent) {
		button = new Button(parent, getStyle());
		button.addSelectionListener(new SelectionAdapter() {
			/**
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				handleDefaultSelection();
			}
		});
		button.addTraverseListener(new TraverseListener() {
			/**
			 * @see org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse.swt.events.TraverseEvent)
			 */
			@Override
			public void keyTraversed(final TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN)
					e.doit = false;
			}
		});
		button.addFocusListener(new FocusAdapter() {
			/**
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			@SuppressWarnings("synthetic-access")
			@Override
			public void focusLost(final FocusEvent e) {
				IdeaCheckboxCellEditor.this.focusLost();
			}
		});
		button.addSelectionListener(getSelectionListener());
		return button;
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#dependsOnExternalFocusListener()
	 */
	@Override
	protected boolean dependsOnExternalFocusListener() {
		return getClass() != IdeaCheckboxCellEditor.class;
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	@Override
	protected Object doGetValue() {
		return new Boolean(button.getSelection());
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		if (button != null)
			button.setFocus();
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(final Object value) {
		Assert.isTrue(button != null && value instanceof Boolean);
		button.removeSelectionListener(getSelectionListener());
		button.setSelection((Boolean) value);
		button.addSelectionListener(getSelectionListener());
	}

	/**
	 * @see org.eclipse.jface.viewers.CellEditor#getLayoutData()
	 */
	@Override
	public LayoutData getLayoutData() {
		final LayoutData layoutData = super.getLayoutData();

		if (button != null && !button.isDisposed()) {
			layoutData.minimumWidth = 25;
			layoutData.horizontalAlignment = SWT.CENTER;
			layoutData.grabHorizontal = true;
		}

		return layoutData;
	}

	/**
	 * Return the selection listener.
	 */
	private SelectionListener getSelectionListener() {
		if (selectionListener == null) {
			selectionListener = new SelectionAdapter() {
				/**
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@SuppressWarnings("synthetic-access")
				@Override
				public void widgetSelected(final SelectionEvent e) {
					valueChanged(true, true);
				}
			};
		}

		return selectionListener;
	}

	protected void handleDefaultSelection() {
		fireApplyEditorValue();
		deactivate();
	}

}
