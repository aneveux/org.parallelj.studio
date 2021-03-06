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
package org.parallelj.ixea.helpers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

/**
 * TextChangeHelper notifies the listener of Selectionable widgets lifecycle
 * events on behalf of the widget(s) it listens to.
 * 
 */
public abstract class SelectionChangeHelper extends AbstractControlChangeHelper {

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.Selection:
			buttonSelected((Control) event.widget);
			break;
		case SWT.DefaultSelection:
			buttonSelected((Control) event.widget);
			break;
		}
	}

	/**
	 * Abstract method notified when a text field has been changed.
	 * 
	 * @param control
	 *            the given control.
	 */
	public abstract void buttonSelected(Control control);

	/**
	 * Registers this helper with the given control to listen for events which
	 * indicate that a change is in progress (or done).
	 * 
	 * @param control
	 *            the given control.
	 */
	public void startListeningTo(Control control) {
		control.addListener(SWT.Selection, this);
		control.addListener(SWT.DefaultSelection, this);
	}

	/**
	 * Registers this helper with the given control to listen for the Enter key.
	 * When Enter is pressed, the change is considered done (this is only
	 * appropriate for single-line Text widgets).
	 * 
	 * @param control
	 *            the given control.
	 */
	public void startListeningForEnter(Control control) {
	}

	/**
	 * Unregisters this helper from a control previously passed to
	 * startListeningTo() and/or startListeningForEnter().
	 * 
	 * @param control
	 *            the given control.
	 */
	public void stopListeningTo(Control control) {
		if ((control != null) && !control.isDisposed()) {
			control.removeListener(SWT.Selection, this);
			control.removeListener(SWT.DefaultSelection, this);
		}
	}

	public void controlChanged(Control control) {
		buttonSelected(control);
	}
}