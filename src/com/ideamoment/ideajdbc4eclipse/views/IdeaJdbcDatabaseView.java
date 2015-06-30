package com.ideamoment.ideajdbc4eclipse.views;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.ideamoment.ideajdbc4eclipse.database.Conn;
import com.ideamoment.ideajdbc4eclipse.database.DataBaseUtils;
import com.ideamoment.ideajdbc4eclipse.database.Tbl;
import com.ideamoment.ideajdbc4eclipse.dialogs.ConnectionDialog;
import com.ideamoment.ideajdbc4eclipse.editors.ClauseEditorInput;
import com.ideamoment.ideajdbc4eclipse.state.StateUtils;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class IdeaJdbcDatabaseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.ideamoment.ideajdbc4eclipse.views.IdeaJdbcDatabaseView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action addConnectionAction;
	private Action connectionAction;
	private Action doubleClickAction;
	
	private FolderTreeNode invisibleRoot;
	
	private TreeObject selectedTreeObject;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class TreeObject implements IAdaptable {
		private String name;
		private FolderTreeNode parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(FolderTreeNode parent) {
			this.parent = parent;
		}
		public FolderTreeNode getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class FolderTreeNode extends TreeObject {
		private ArrayList children;
		
		public FolderTreeNode(String name) {
			super(name);
			children = new ArrayList();
		}
		
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}
	
	class DatabaseTreeNode extends FolderTreeNode {
		private ArrayList children;
		private String url;
		private String userName;
		private String password;
		
		public DatabaseTreeNode(String name) {
			super(name);
			children = new ArrayList();
		}
		
		public DatabaseTreeNode(String name, String url, String userName, String password) {
			super(name);
			children = new ArrayList();
			this.url = url;
			this.userName = userName;
			this.password = password;
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
		public String getUrl() {
			return this.url;
		}
		public String getUserName() {
			return this.userName;
		}
		public String getPassword() {
			return this.password;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof DatabaseTreeNode) {
				return ((DatabaseTreeNode)child).getParent();
			}
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof FolderTreeNode)
				return ((FolderTreeNode)parent).getChildren();
			if (parent instanceof DatabaseTreeNode) {
				return ((DatabaseTreeNode)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof FolderTreeNode)
				return ((FolderTreeNode)parent).hasChildren();
			if (parent instanceof DatabaseTreeNode)
				return ((DatabaseTreeNode)parent).hasChildren();
			return false;
		}
/*
 * We will set up a dummy model to initialize tree heararchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		private void initialize() {
			FolderTreeNode root = new FolderTreeNode("Database");
			
			List<Conn> conns = StateUtils.getAllConns();
			for(Conn conn : conns) {
				DatabaseTreeNode connNode = new DatabaseTreeNode(conn.getName(), conn.getUrl(), conn.getUserName(), conn.getPassword());
				root.addChild(connNode);
			}
			
			invisibleRoot = new FolderTreeNode("");
			invisibleRoot.addChild(root);
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof DatabaseTreeNode)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public IdeaJdbcDatabaseView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
		   public void selectionChanged(SelectionChangedEvent event) {
		       // if the selection is empty clear the label
		       if(event.getSelection().isEmpty()) {
		           return;
		       }
		       if(event.getSelection() instanceof IStructuredSelection) {
		           IStructuredSelection selection = (IStructuredSelection)event.getSelection();
		           StringBuffer toShow = new StringBuffer();
		           for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
		        	   selectedTreeObject = (TreeObject) iterator.next();
		               break;
		           }
		       }
		   }
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "ideajdbc4eclipse.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				IdeaJdbcDatabaseView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addConnectionAction);
		manager.add(new Separator());
		manager.add(connectionAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addConnectionAction);
		manager.add(connectionAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addConnectionAction);
		manager.add(connectionAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		addConnectionAction = new Action() {
			public void run() {
				ConnectionDialog connDialog = new ConnectionDialog(viewer.getControl().getShell());
				connDialog.open();
			}
		};
		addConnectionAction.setText("Add Connection");
		addConnectionAction.setToolTipText("Add Connection");
		addConnectionAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		connectionAction = new Action() {
			public void run() {
				if(selectedTreeObject == null) {
					showMessage("你啥也没选啊，逗我？");
				}else{
					if(selectedTreeObject instanceof DatabaseTreeNode) {
						DatabaseTreeNode dtn = (DatabaseTreeNode)selectedTreeObject;
						
						Connection conn = DataBaseUtils.getConnection(dtn.getUrl(), dtn.getUserName(), dtn.getPassword());
						List<Tbl> tbls = DataBaseUtils.getAllTables(conn);
						
						TreeObject[] children = ((FolderTreeNode)invisibleRoot.getChildren()[0]).getChildren();
						for(TreeObject tObj : children) {
							if(tObj instanceof DatabaseTreeNode) {
								if(tObj.getName().equals(dtn.getName())) {
									DatabaseTreeNode node = (DatabaseTreeNode)tObj;
									for(Tbl tbl : tbls) {
										TreeObject tableNode = new TreeObject(tbl.getName());
										((DatabaseTreeNode) tObj).addChild(tableNode);
									}
									break;
								}
							}
						}
						
						viewer.refresh();
					}else{
						showMessage("别开玩笑了，你觉得这个东东能连接吗？");
					}
				}
			}
		};
		connectionAction.setText("Connection");
		connectionAction.setToolTipText("Action 2 tooltip");
		connectionAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				if(obj.getClass().getName().indexOf("TreeObject") > -1) {
					TreeObject tObj = (TreeObject)obj;
					String tblName = tObj.getName();
					
					DatabaseTreeNode dbTreeNode = (DatabaseTreeNode)tObj.getParent();
					Conn connInfo = new Conn();
					connInfo.setName(dbTreeNode.getName());
					connInfo.setUrl(dbTreeNode.getUrl());
					connInfo.setUserName(dbTreeNode.getUserName());
					connInfo.setPassword(dbTreeNode.getPassword());
					
					ClauseEditorInput editorInput = new ClauseEditorInput();
					editorInput.setName(tblName);
					editorInput.setConnInfo(connInfo);
					
					IWorkbenchPage workbenchPage = getViewSite().getPage();
					
					IEditorPart editor = workbenchPage.findEditor(editorInput);
					
					if(editor != null) {
						workbenchPage.bringToTop(editor);
					}else{
						try {
							workbenchPage.openEditor(editorInput, "com.ideamoment.ideajdbc4eclipse.editors.ClauseEditor");
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				}else{
					showMessage("这个不可以打开编辑器");
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"IdeaJdbc Database",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}