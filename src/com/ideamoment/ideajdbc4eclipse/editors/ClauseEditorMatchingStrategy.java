/**
 * 
 */
package com.ideamoment.ideajdbc4eclipse.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import com.ideamoment.ideajdbc4eclipse.database.Tbl;

/**
 * @author Chinakite
 * 
 */
public class ClauseEditorMatchingStrategy implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		IEditorInput editorInput = null;

		try {
			editorInput = editorRef.getEditorInput();
		} catch (PartInitException ex) {
			ex.printStackTrace();
		}

		if (editorInput == null)
			return false;

		if (input == null)
			return false;

		if (!(editorInput instanceof ClauseEditorInput))
			return false;

		ClauseEditorInput input1 = (ClauseEditorInput)editorInput;
		
		if (!(input instanceof ClauseEditorInput))
			return false;

		ClauseEditorInput input2 = (ClauseEditorInput)input;
		
		if (input1.getName().equals(input2.getName()) && input1.getConnInfo().getName().equals(input2.getConnInfo().getName())) {
			return true;
		} else {
			return false;
		}
	}

}
