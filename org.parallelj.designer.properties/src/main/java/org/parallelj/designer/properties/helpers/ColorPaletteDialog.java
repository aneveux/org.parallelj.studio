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
package org.parallelj.designer.properties.helpers;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.gmf.runtime.common.ui.util.WindowUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ColorPaletteDialog {

	/** variable to store previous color */
	private int previousColor;

	private Button customColorButton;

	private HashMap<RGB, Button> buttonMap = new HashMap<RGB, Button>();

	/**
	 * A descriptor for an inventory color
	 */
	private static class InventoryColorDescriptor extends ImageDescriptor {

		/** the default preference color */
		private static final RGB OUTLINE_COLOR = new RGB(192, 192, 192);

		public RGB rgb;

		public InventoryColorDescriptor(RGB colorValue) {
			this.rgb = colorValue;

		}

		/**
		 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
		 */
		public ImageData getImageData() {
			ImageData data = new ImageData(ICON_SIZE.x, ICON_SIZE.y, 1,
					new PaletteData(new RGB[] { rgb, OUTLINE_COLOR }));

			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(0, i, 1);
			for (int i = 0; i < ICON_SIZE.y; i++)
				data.setPixel(ICON_SIZE.x - 1, i, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, 0, 1);
			for (int i = 0; i < ICON_SIZE.x; i++)
				data.setPixel(i, ICON_SIZE.y - 1, 1);
			return data;
		}

		/**
		 * Creates and returns a new SWT image for this image descriptor. The
		 * returned image must be explicitly disposed using the image's dispose
		 * call. The image will not be automatically garbage collected. In the
		 * even of an error, a default image is returned.
		 */
		public Image createImage() {

			Device device = Display.getCurrent();
			ImageData data = getImageData();
			if (data == null)
				data = DEFAULT_IMAGE_DATA;

			/*
			 * Try to create the supplied image. If there is an SWT Exception
			 * try and create the default image if that was requested. Return
			 * null if this fails.
			 */

			try {
				if (data.transparentPixel >= 0) {
					ImageData maskData = data.getTransparencyMask();
					return new Image(device, data, maskData);
				}
				return new Image(device, data);
			} catch (SWTException exception) {

				try {
					return new Image(device, DEFAULT_IMAGE_DATA);
				} catch (SWTException nextException) {
					return null;
				}

			}
		}
	}

	/** default color icon width */
	public static final Point ICON_SIZE = new Point(
			IDialogConstants.BUTTON_BAR_HEIGHT,
			IDialogConstants.BUTTON_BAR_HEIGHT);

	/** inventory colors */
	private static final InventoryColorDescriptor WHITE = new InventoryColorDescriptor(
			new RGB(255, 255, 255));

	private static final InventoryColorDescriptor BLACK = new InventoryColorDescriptor(
			new RGB(0, 0, 0));

	private static final InventoryColorDescriptor LIGHT_GRAY = new InventoryColorDescriptor(
			new RGB(192, 192, 192));

	private static final InventoryColorDescriptor GRAY = new InventoryColorDescriptor(
			new RGB(128, 128, 128));

	private static final InventoryColorDescriptor RED = new InventoryColorDescriptor(
			new RGB(227, 164, 156));

	private static final InventoryColorDescriptor GREEN = new InventoryColorDescriptor(
			new RGB(166, 193, 152));

	private static final InventoryColorDescriptor BLUE = new InventoryColorDescriptor(
			new RGB(152, 168, 191));

	private static final InventoryColorDescriptor YELLOW = new InventoryColorDescriptor(
			new RGB(225, 225, 135));

	private static final InventoryColorDescriptor PURPLE = new InventoryColorDescriptor(
			new RGB(184, 151, 192));

	private static final InventoryColorDescriptor TEAL = new InventoryColorDescriptor(
			new RGB(155, 199, 204));

	private static final InventoryColorDescriptor PINK = new InventoryColorDescriptor(
			new RGB(228, 179, 229));

	private static final InventoryColorDescriptor ORANGE = new InventoryColorDescriptor(
			new RGB(237, 201, 122));

	private static final HashMap<RGB, Image> imageColorMap = new HashMap<RGB, Image>();

	private static final String DEFAULT_COLOR_STRING = "Default";

	private static final String CUSTOM_COLOR_STRING = "Custom ...";

	static {

		// inventory colors
		imageColorMap.put(WHITE.rgb, WHITE.createImage());
		imageColorMap.put(BLACK.rgb, BLACK.createImage());
		imageColorMap.put(LIGHT_GRAY.rgb, LIGHT_GRAY.createImage());
		imageColorMap.put(GRAY.rgb, GRAY.createImage());
		imageColorMap.put(RED.rgb, RED.createImage());
		imageColorMap.put(GREEN.rgb, GREEN.createImage());
		imageColorMap.put(BLUE.rgb, BLUE.createImage());
		imageColorMap.put(YELLOW.rgb, YELLOW.createImage());
		imageColorMap.put(PURPLE.rgb, PURPLE.createImage());
		imageColorMap.put(TEAL.rgb, TEAL.createImage());
		imageColorMap.put(PINK.rgb, PINK.createImage());
		imageColorMap.put(ORANGE.rgb, ORANGE.createImage());
	}

	private Shell shell;

	private RGB selectedColor = null;

	/**
	 * The default color to be used if the user presses the default button.
	 */
	private boolean useDefaultColor = false;

	/**
	 * Creates a PopupList above the specified shell.
	 */
	public ColorPaletteDialog(Shell parent, int rowHeight) {

		shell = new Shell(parent, checkStyle(SWT.NONE));
		shell.setLayout(new FillLayout());
		GridLayout layout = new GridLayout(4, true);
		shell.setLayout(layout);

		for (Iterator<RGB> e = imageColorMap.keySet().iterator(); e.hasNext();) {
			Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
			GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			data.heightHint = rowHeight;
			data.widthHint = rowHeight;
			button.setLayoutData(data);

			final RGB rgb = (RGB) e.next();
			final Image image = (Image) imageColorMap.get(rgb);
			button.setImage(image);
			button.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e1) {
					selectedColor = rgb;
					shell.dispose();
				}
			});
			buttonMap.put(rgb, button);
		}
		Button defaultButton = new Button(shell, SWT.PUSH | SWT.FLAT);
		defaultButton.setText(DEFAULT_COLOR_STRING);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 4;
		defaultButton.setLayoutData(data);

		defaultButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				useDefaultColor = true;
				shell.dispose();
			}
		});

		Button moreColors = new Button(shell, SWT.PUSH | SWT.FLAT);
		moreColors.setText(CUSTOM_COLOR_STRING);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 4;
		moreColors.setLayoutData(data);

		moreColors.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				ColorDialog dialog = new ColorDialog(Display.getCurrent()
						.getActiveShell());
				dialog.setRGB(FigureUtilities.integerToRGB(getPreviousColor()));
				WindowUtil.centerDialog(dialog.getParent(), Display
						.getCurrent().getActiveShell());
				dialog.open();
				selectedColor = dialog.getRGB();
				shell.dispose();

			}
		});
		customColorButton = moreColors;
	}

	private static int checkStyle(int style) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return style & mask;
	}

	/**
	 * Launches the Pop up List, waits for an item to be selected and then
	 * closes PopupList
	 */
	public RGB open(Point location) {

		Point listSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		shell.setBounds(location.x, location.y, listSize.x, listSize.y);

		shell.open();
		shell.setFocus();
		Display display = shell.getDisplay();
		Button prevButton = (Button) buttonMap.get(FigureUtilities
				.integerToRGB(getPreviousColor()));
		if (prevButton != null) {
			shell.setDefaultButton(prevButton);
		} else {
			shell.setDefaultButton(customColorButton);
		}
		while (!shell.isDisposed() && shell.isVisible()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return getSelectedColor();
	}

	/**
	 * Gets the color the user selected. Could be null as the user may have
	 * Canceled the gesture or they may have selected the default color button.
	 * 
	 */
	public RGB getSelectedColor() {
		return selectedColor;
	}

	/**
	 * Returns true if the user selected to use the default color
	 */
	public boolean useDefaultColor() {
		return useDefaultColor;
	}

	public int getPreviousColor() {
		return previousColor;
	}

	public void setPreviousColor(int previousColor) {
		this.previousColor = previousColor;
	}

}
