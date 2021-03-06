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
package org.parallelj.ixea.tools;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ComboViewer adapted to CCombo
 *
 * @author mvanbesien
 * @version $Revision: 1.1 $
 * @since $Date: 2008/01/21 16:39:19 $
 * 
 */
public class CComboViewer extends AbstractListViewer {

	/**
	 * This viewer's list control.
	 */
	private CCombo cCombo;

	/**
	 * Creates a combo viewer on a newly-created combo control under the given
	 * parent. The viewer has no input, no content provider, a default label
	 * provider, no sorter, and no filters.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public CComboViewer(Composite parent) {
		this(parent, SWT.READ_ONLY | SWT.BORDER);
	}

	/**
	 * Creates a combo viewer on a newly-created combo control under the given
	 * parent. The combo control is created using the given SWT style bits. The
	 * viewer has no input, no content provider, a default label provider, no
	 * sorter, and no filters.
	 * 
	 * @param parent
	 *            the parent control
	 * @param style
	 *            the SWT style bits
	 */
	public CComboViewer(Composite parent, int style) {
		this(new CCombo(parent, style));
	}

	/**
	 * Creates a combo viewer on the given combo control. The viewer has no
	 * input, no content provider, a default label provider, no sorter, and no
	 * filters.
	 * 
	 * @param list
	 *            the combo control
	 */
	public CComboViewer(CCombo list) {
		this.cCombo = list;
		hookControl(list);
	}

	protected void listAdd(String string, int index) {
		cCombo.add(string, index);
	}

	protected void listSetItem(int index, String string) {
		cCombo.setItem(index, string);
	}

	protected int[] listGetSelectionIndices() {
		return new int[] { cCombo.getSelectionIndex() };
	}

	protected int listGetItemCount() {
		return cCombo.getItemCount();
	}

	protected void listSetItems(String[] labels) {
		cCombo.setItems(labels);
	}

	protected void listRemoveAll() {
		cCombo.removeAll();
	}

	protected void listRemove(int index) {
		cCombo.remove(index);
	}

	/*
	 * (non-Javadoc) Method declared on Viewer.
	 */
	public Control getControl() {
		return cCombo;
	}

	/**
	 * Returns this list viewer's list control.
	 * 
	 * @return the list control
	 */
	public CCombo getCombo() {
		return cCombo;
	}

	/*
	 * Do nothing -- combos only display the selected element, so there is no
	 * way we can ensure that the given element is visible without changing the
	 * selection. Method defined on StructuredViewer.
	 */
	public void reveal(Object element) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listSelectAndShow(int[])
	 */
	protected void listSetSelection(int[] ixs) {
		for (int idx = 0; idx < ixs.length; idx++) {
			cCombo.select(ixs[idx]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listDeselectAll()
	 */
	protected void listDeselectAll() {
		cCombo.deselectAll();
		cCombo.clearSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listShowSelection()
	 */
	protected void listShowSelection() {

	}

}
