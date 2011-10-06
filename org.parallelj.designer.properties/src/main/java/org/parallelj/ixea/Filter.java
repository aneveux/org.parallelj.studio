/*
 *     ParallelJ, framework for parallel computing
 *     
 *     Copyright (C) 2010 Atos Worldline or third-party contributors as
 *     indicated by the @author tags or express copyright attribution
 *     statements applied by the authors.
 *     
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License.
 *     
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *     Lesser General Public License for more details.
 *     
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.parallelj.ixea;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.viewers.IFilter;

/**
 * This abstract class is used by the properties view to determine the type of
 * the selected Object, in order to display the corresponding properties page
 * represented by Sections
 * 
 * @see Section
 * @author Atos Worldline
 * 
 */
public abstract class Filter implements IFilter {

	/**
	 * Method which purpose is to return whether the object passed as parameter
	 * is the object to be displayed in the Properties View.
	 * 
	 */
	public abstract boolean select(Object toTest);

	/**
	 * Method that returns true if the tested Object is an Instance of the
	 * EObject Class
	 * 
	 * Shortcuts always return false
	 * 
	 * @param toTest
	 *            : Object to test
	 * @param eObjectClass
	 *            : Class used to check if the Object is an instance of this.
	 * @return true is test is valid, else otherwise
	 */
	public final boolean select(Object toTest, Class<?> eObjectClass) {
		return this.select(toTest, eObjectClass, false);
	}

	/**
	 * Method that returns true if the tested Object is an Instance of the
	 * EObject Class
	 * 
	 * @param toTest
	 *            : Object to test
	 * @param eObjectClass
	 *            : Class used to check if the Object is an instance of this.
	 * @param allowShortcut
	 *            : true to allow shortcut presence, false otherwise
	 * @return true is test is valid, else otherwise
	 */
	public final boolean select(Object toTest, Class<?> eObjectClass,
			boolean allowShortcuts) {
		if (!allowShortcuts && this.isShortcut(toTest))
			return false;
		EObject eObject = convertToEMF(toTest);
		return (eObjectClass.isInstance(eObject));
	}

	/**
	 * Method that returns true if the tested Object is an Instance of an
	 * EObject Class, AND if the tested Object is not instance of another
	 * EObject Class.
	 * 
	 * Shortcuts always return false
	 * 
	 * @param toTest
	 *            : Object to test
	 * @param eObjectClass
	 *            : Class used to check if the Object is an instance of this.
	 * @param notEObjectClass
	 *            : Class used to check if the Object is not an instance of
	 *            this.
	 * @return
	 */
	public final boolean select(Object toTest, Class<?> eObjectClass,
			Class<?> notEObjectClass) {
		return this.select(toTest, eObjectClass, notEObjectClass, false);
	}

	/**
	 * Method that returns true if the tested Object is an Instance of an
	 * EObject Class, AND if the tested Object is not instance of another
	 * EObject Class
	 * 
	 * @param toTest
	 *            : Object to test
	 * @param eObjectClass
	 *            : Class used to check if the Object is an instance of this.
	 * @param notEObjectClass
	 *            : Class used to check if the Object is not an instance of
	 *            this.
	 * @return
	 */
	public final boolean select(Object toTest, Class<?> eObjectClass,
			Class<?> notEObjectClass, boolean allowShortcuts) {
		if (!allowShortcuts && this.isShortcut(toTest))
			return false;
		EObject eObject = convertToEMF(toTest);
		return (eObjectClass.isInstance(eObject) && !(notEObjectClass
				.isInstance(eObject)));
	}

	/**
	 * Method used to retrieve the EObject linked with the Object passed as
	 * parameter
	 * 
	 * @param object
	 *            : Object from which EObject has to be revealed
	 * @return EObject
	 */
	protected final EObject convertToEMF(Object object) {
		EObject eObject = null;
		if (object instanceof GraphicalEditPart) {
			AbstractGraphicalEditPart myGEP = (AbstractGraphicalEditPart) object;
			Object model = myGEP.getModel();
			if (model instanceof View)
				eObject = ((View) model).getElement();
			return eObject;
		} else if (object instanceof EObject) {
			return (EObject) object;
		} else {
			return null;
		}
	}

	/**
	 * Tests if Object passed as parameter is an EditPart, and if true, tests
	 * whether this EditPart is a Shortcut
	 * 
	 * @param object
	 *            : Object to test
	 * @return true if Shortcut, false if not EditPart and/or is not Shortcut
	 */
	protected final boolean isShortcut(Object object) {
		if (object instanceof EditPart) {
			EditPart editPart = (EditPart) object;
			Diagram diagram = null;
			if (editPart.getModel() instanceof Diagram) {
				return false;
			} else {
				diagram = ((View) editPart.getModel()).getDiagram();
			}

			EObject diagramElement = diagram.getElement();

			View selectedView = (View) editPart.getModel();
			EObject selectedElement = selectedView.getElement();

			EObject eObject = selectedElement;
			while (eObject != null) {
				if (diagramElement.equals(eObject))
					return false;
				eObject = eObject.eContainer();
			}
			return true;
		}
		return false;
	}
}
