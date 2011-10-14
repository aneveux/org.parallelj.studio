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

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * This Abstract Class role is to define Items. An Item is a Composite part
 * containing information, that can be created in Zones. A particularity of this
 * kind of Object, is to allow reloading and visibility setting in a process.
 * 
 * @see Zone
 * @author Atos Worldline
 * 
 */
public abstract class Item {

	/**
	 * Composite Background.
	 */
	private Composite backGround = null;

	/**
	 * BackGround parent.
	 */
	private Composite parent = null;

	/**
	 * Tells if BackGround (full Item) should be visible or not.
	 */
	private boolean isVisible = true;

	/**
	 * FormData used be SubClasses to define this item's Objects layouts
	 */
	protected FormData fData;

	/**
	 * TabbedPropertySheetWidgetFactory Instance, used to create Elements.
	 */
	private TabbedPropertySheetWidgetFactory widgetFactory;

	/**
	 * Constructor
	 * 
	 * @param parent :
	 *            Parent in which this Item will be drawn
	 * @param widgetFactory :
	 *            Instance used to create Elements.
	 */
	public Item(Composite parent, TabbedPropertySheetWidgetFactory widgetFactory) {

		this.parent = parent;
		this.widgetFactory = widgetFactory;

		this.backGround = widgetFactory.createComposite(this.parent);
		this.backGround.setLayout(new FormLayout());

		initElements();
		setLayoutsToElements();
		setListenersToElements();
	}

	/**
	 * Default method used to reload this Item
	 * 
	 */
	public void load() {
		makeVisible();
		if (isVisible())
			backGround.update();

	}

	/**
	 * Set the property holding the visibility parameter
	 * 
	 * @param visible :
	 *            should be true, to set this Item Visible, false otherwise
	 */
	public final void setVisible(boolean visible) {
		this.isVisible = visible;
		makeVisible();
	}

	/**
	 * Makes the Item visible or not, by using the isVisible property.
	 * 
	 */
	public final void makeVisible() {
		backGround.setEnabled(this.isVisible);
		backGround.setVisible(this.isVisible);
	}

	/**
	 * Tells if Item is visible
	 * 
	 * @return true if visible, false otherwise
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Method which purpose is to create and initialize all the graphical
	 * elements to be displayed in this Item
	 * 
	 */
	protected abstract void initElements();

	/**
	 * Method which purpose is to create and set the layout between all the
	 * graphical elements to be displayed in this Item
	 * 
	 */
	protected abstract void setLayoutsToElements();

	/**
	 * Method which purpose is to create and set the listeners to the graphical
	 * elements to be displayed in this Item
	 * 
	 */
	protected abstract void setListenersToElements();

	/**
	 * Method which purpose is to update the values of the elements to be
	 * displayed in this Item
	 * 
	 */
	protected abstract void updateValues();

	/**
	 * Gets this Item's background
	 * 
	 * @return Composite standing for Item's background
	 */
	public Composite getBackGround() {
		return backGround;
	}

	/**
	 * Gets the Widget Factory Instance
	 * 
	 * @return Widget Factory Instance
	 */
	protected TabbedPropertySheetWidgetFactory getWidgetFactory() {
		return widgetFactory;
	}
}