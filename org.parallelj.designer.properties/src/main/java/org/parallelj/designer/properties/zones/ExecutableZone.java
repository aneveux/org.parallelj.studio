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
package org.parallelj.designer.properties.zones;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipselabs.resourceselector.core.processor.ResourceProcessorFactory;
import org.eclipselabs.resourceselector.core.resources.ResourceInfo;
import org.eclipselabs.resourceselector.core.selector.ResourceSelector;
import org.eclipselabs.resourceselector.processor.java.hierarchy.JavaHierarchyTypeProcessorFactory;
import org.parallelj.designer.properties.helpers.ParallelJPropertiesMessages;
import org.parallelj.designer.properties.helpers.Tools;
import org.parallelj.ixea.Zone;
import org.parallelj.ixea.helpers.TextChangeHelper;
import org.parallelj.ixea.tools.Commands;
import org.parallelj.ixea.tools.FormDataBuilder;
import org.parallelj.model.ParallelJPackage;
import org.parallelj.model.Procedure;

@SuppressWarnings("restriction")
public class ExecutableZone extends Zone {

	private CLabel executableLabel;

	private Text executableText;

	private Button createExecutable;

	private Button selectExecutable;

	public ExecutableZone(Composite parent, boolean isGroup) {
		super(parent, isGroup);
	}

	@Override
	public void addItemsToZone() {
		executableLabel = this.getWidgetFactory().createCLabel(getZone(),
				ParallelJPropertiesMessages.element_executable.message());
		executableText = this.getWidgetFactory().createText(getZone(), "",
				SWT.READ_ONLY);
		executableText.setEditable(false);
		createExecutable = this.getWidgetFactory().createButton(getZone(),
				ParallelJPropertiesMessages.button_create.message(), SWT.PUSH);
		selectExecutable = this.getWidgetFactory().createButton(getZone(),
				ParallelJPropertiesMessages.button_select.message(), SWT.PUSH);
	}

	@Override
	public void addLayoutsToItems() {
		new FormDataBuilder().left().top().mediumLabel().apply(executableLabel);
		new FormDataBuilder().left(executableLabel).right().top()
				.apply(executableText);
		new FormDataBuilder().top(executableText).right().shortButton()
				.apply(selectExecutable);
		new FormDataBuilder().top(executableText).right(selectExecutable)
				.shortButton().apply(createExecutable);
	}

	@Override
	public void addListenersToItems() {
		TextChangeHelper executableTextListener = new TextChangeHelper() {
			@Override
			public void textChanged(Control control) {
				String value = ((Text) control).getText();
				String formattedValue = value;
				Commands.doSetValue(getEditingDomain(), getEObject(),
						ParallelJPackage.eINSTANCE.getProcedure_Executable(),
						formattedValue, getEditPart());
			}
		};
		executableTextListener.startListeningTo(executableText);
		executableTextListener.startListeningForEnter(executableText);
		selectExecutable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ExecutableZone.this.askForExecutable();
			}
		});
		createExecutable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ExecutableZone.this.createExecutable();
			}
		});
	}

	@Override
	public void updateItemsValues() {
		Object o = getEObject().eGet(
				ParallelJPackage.eINSTANCE.getProcedure_Executable());
		executableText.setText(o != null ? (String) o : "");
	}

	protected void askForExecutable() {
		ResourceProcessorFactory[] factories = new ResourceProcessorFactory[] {
				// new ParallelJModelResourceProcessorFactory(getEObject()
				// .eResource().getResourceSet()),
				new JavaHierarchyTypeProcessorFactory(Runnable.class),
				new JavaHierarchyTypeProcessorFactory(Callable.class) };
		ResourceSelector selector = new ResourceSelector(factories, Tools
				.getJavaProjectFromEObject(getEObject()).getProject());
		selector.run();
		ResourceInfo savedTypeInfo = selector.getSavedResourceInfo();

		if (savedTypeInfo == null)
			return;

		String newType = savedTypeInfo.getPackageName() + "."
				+ savedTypeInfo.getElementName();
		if (getEObject() != null && getEObject() instanceof Procedure) {
			if (!newType.equals(((Procedure) getEObject()).getExecutable())) {
				Commands.doSetValue(getEditingDomain(), getEObject(),
						ParallelJPackage.eINSTANCE.getProcedure_Executable(),
						newType, getEditPart());
				this.executableText.setText(newType);
			}
		}
	}

	protected void createExecutable() {
		CustomNewClassCreationWizard wizard = new CustomNewClassCreationWizard();
		WizardDialog dialog = new WizardDialog(getZone().getShell(), wizard);
		dialog.open();
		String createdType = wizard.getCreatedType();
		if (createdType == null)
			return;

		if (getEObject() != null && getEObject() instanceof Procedure) {
			if (!createdType.equals(((Procedure) getEObject()).getExecutable())) {
				Commands.doSetValue(getEditingDomain(), getEObject(),
						ParallelJPackage.eINSTANCE.getProcedure_Executable(),
						createdType, getEditPart());
				this.executableText.setText(createdType);
			}
		}
	}

	class CustomNewClassCreationWizard extends NewElementWizard {
		private NewClassWizardPage page;

		public CustomNewClassCreationWizard() {
			setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
			setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
			setWindowTitle(NewWizardMessages.NewClassCreationWizard_title);
			page = new NewClassWizardPage();
			List<String> interfaces = new ArrayList<String>();
			interfaces.add("java.lang.Runnable");
			page.setSuperInterfaces(interfaces, false);
			page.setMethodStubSelection(false, true, true, false);
		}

		@Override
		public void addPages() {
			super.addPages();
			addPage(page);
		}

		@Override
		protected boolean canRunForked() {
			return !page.isEnclosingTypeSelected();
		}

		@Override
		protected void finishPage(IProgressMonitor monitor)
				throws InterruptedException, CoreException {
			page.createType(monitor);
		}

		@Override
		public boolean performFinish() {
			warnAboutTypeCommentDeprecation();
			return super.performFinish();
		}

		@Override
		public IJavaElement getCreatedElement() {
			return page.getCreatedType();
		}

		public String getCreatedType() {
			return page.getCreatedType().getFullyQualifiedName();
		}

	}

	// class ParallelJModelResourceProcessorFactory implements
	// ResourceProcessorFactory {
	//
	// protected ResourceSet resourceSet;
	//
	// public ParallelJModelResourceProcessorFactory(ResourceSet resourceSet) {
	// this.resourceSet = resourceSet;
	// }
	//
	// @Override
	// public ResourceProcessor createResourceProcessor(IProject project,
	// String pattern) {
	// return new ParallelJModelResourceProcessor(this.resourceSet,
	// project, pattern);
	// }
	//
	// }

	// class ParallelJModelResourceProcessor extends ResourceProcessor {
	//
	// IProject project;
	// String pattern;
	// ResourceSet resourceSet;
	//
	// public ParallelJModelResourceProcessor(ResourceSet resourceSet,
	// IProject project, String pattern) {
	// this.resourceSet = resourceSet;
	// this.project = project;
	// this.pattern = pattern;
	// }
	//
	// @Override
	// protected void process() {
	// for (Resource resource : resourceSet.getResources()) {
	// for (EObject eObject : resource.getContents()) {
	// if (eObject instanceof Specification) {
	// Specification spec = (Specification) eObject;
	// for (Program program : spec.getPrograms()) {
	// if (this.pattern.equals("*"))
	// this.searchResults
	// .add(getFromProgramName(program
	// .getName()));
	// else if (program.getName().contains(this.pattern))
	// this.searchResults
	// .add(getFromProgramName(program
	// .getName()));
	// else if (this.pattern.endsWith("*")
	// && program.getName().startsWith(
	// this.pattern.substring(0,
	// this.pattern.length() - 2)))
	// this.searchResults
	// .add(getFromProgramName(program
	// .getName()));
	// }
	// }
	// }
	// }
	// }
	//
	// private ResourceInfo getFromProgramName(String name) {
	// List<String> parts = Arrays.asList(name.split("."));
	// if (parts != null && parts.size() > 1) {
	// StringBuilder packageName = new StringBuilder();
	// for (int i = 0; i < parts.size() - 1; i++)
	// packageName.append(parts.get(i));
	// return new ResourceInfo(parts.get(parts.size() - 1),
	// packageName.toString(), project.getFullPath()
	// .toString()) {
	// };
	// } else
	// return null;
	// }
	// }

}
