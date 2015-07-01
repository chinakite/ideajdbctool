/**
 * 
 */
package com.ideamoment.ideajdbctool.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ideamoment.ideajdbctool.state.StateUtils;

/**
 * @author Chinakite
 *
 */
public class ConnectionDialog extends TitleAreaDialog {

	private static final int TEST_CONNECTION_ID = 10001;
	
	private Text nameText;
	private Text urlText;
	private Text userNameText;
	private Text passwordText;
	
	public ConnectionDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // TitleArea中的Title
        setTitle("Connection");

        // TitleArea中的Message
        setMessage("填写数据库连接相关信息。注意：目前只支持MySQL");

        // TitleArea中的Image
        //setTitleImage(ResourceManager.getPluginImage(IdeaJdbcActivator.getDefault(), "icons/Neptune.png"));

        container.setLayout(new GridLayout(4, false));
        
        Label nameLabel = new Label(container, SWT.NONE);
        nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        nameLabel.setText("名称");
         
        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label urlLabel = new Label(container, SWT.NONE);
        urlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        urlLabel.setText("URL");
         
        urlText = new Text(container, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label userNameLabel = new Label(container, SWT.NONE);
        userNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        userNameLabel.setText("用户名");
         
        userNameText = new Text(container, SWT.BORDER);
        userNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Label passwordLabel = new Label(container, SWT.NONE);
        passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        passwordLabel.setText("密码");
         
        passwordText = new Text(container, SWT.BORDER);
        passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        return area;
	}

	@Override
	protected Point getInitialSize() {
		Point p = super.getInitialSize();
		p.x = 500;
		p.y = 400;
		return p;
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        // Dialog Title
        newShell.setText("Add Connection");

        // Dialog Icon
//        newShell.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/Neptune.png"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.TrayDialog#createButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected Control createButtonBar(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 0;
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);

        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        if (isHelpAvailable()) {
            createHelpControl(composite);
        }

        createButton(composite, TEST_CONNECTION_ID, "测试连接", false).addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                
            }
        });

        Label filler = new Label(composite, SWT.NONE);
        filler.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        layout.numColumns++;

        createButton(composite, IDialogConstants.OK_ID, "确定", true).addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	System.out.println("========");
            	System.out.println(StateUtils.getStateStoreFile());
            }
        });
        createButton(composite, IDialogConstants.CANCEL_ID, "取消", false);

        return composite;
    }
	
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.TrayDialog#isHelpAvailable()
     */
    public boolean isHelpAvailable() {
        return false;
    }
}
