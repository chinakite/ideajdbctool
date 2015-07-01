/**
 * 
 */
package com.ideamoment.ideajdbctool.table;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.internal.databinding.beans.BeanPropertyHelper;

/**
 * @author Chinakite
 *
 */
@SuppressWarnings("restriction")
public class PropertyValue implements IValue {

	private final Object[] properties;

	/**
	 * Creates a PropertyValue to access the bean property referred by
	 * propertyName.
	 */
	public PropertyValue(String propertyName) {
		properties = split(propertyName);

	}

	private static Object[] split(String propertyName) {
		if (propertyName.indexOf('.') == -1)
			return new Object[] { propertyName };
		List<String> propertyNames = new ArrayList<String>();
		int index;
		while ((index = propertyName.indexOf('.')) != -1) {
			propertyNames.add(propertyName.substring(0, index));
			propertyName = propertyName.substring(index + 1);
		}
		propertyNames.add(propertyName);
		return propertyNames.toArray(new Object[propertyNames.size()]);
	}

	/**
	 * Returns the property value referred by object."propertyName"
	 */
	public Object getValue(Object object) {
		Object currentObject = object;
		for (int i = 0; i < properties.length; i++) {
			if (currentObject == null)
				return null;

			if (properties[i] instanceof String) {
				properties[i] = BeanPropertyHelper.getPropertyDescriptor(currentObject.getClass(),
						(String) properties[i]);
			}

			if (properties[i] instanceof PropertyDescriptor) {
				currentObject = BeanPropertyHelper.readProperty(currentObject, (PropertyDescriptor) properties[i]);
			} else {
				throw new RuntimeException("Invalid property: " + properties[i]);
			}

		}

		return currentObject;
	}

	/**
	 * Sets the property value referred by object."propertyName" to value
	 */
	public void setValue(Object object, Object value) {
		Object currentObject = object;
		for (int i = 0; i < properties.length; i++) {
			if (currentObject == null)
				throw new RuntimeException("Value cannot be set because of null value in nested property!");

			if (properties[i] instanceof String) {
				properties[i] = BeanPropertyHelper.getPropertyDescriptor(currentObject.getClass(),
						(String) properties[i]);
			}

			if (properties[i] instanceof PropertyDescriptor) {
				boolean lastProperty = (i == properties.length - 1);
				if (lastProperty)
					BeanPropertyHelper.writeProperty(currentObject, (PropertyDescriptor) properties[i], value);
				else
					currentObject = BeanPropertyHelper.readProperty(currentObject, (PropertyDescriptor) properties[i]);
			} else {
				throw new RuntimeException("Invalid property: " + properties[i]);
			}

		}
	}

}
