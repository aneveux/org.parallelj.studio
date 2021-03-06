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
package org.parallelj.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.parallelj.model.MetaInformation;
import org.parallelj.model.MetaInformationContainer;
import org.parallelj.model.ParallelJPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Meta Information Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.parallelj.model.impl.MetaInformationContainerImpl#getMetaInformation <em>Meta Information</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MetaInformationContainerImpl extends EObjectImpl implements MetaInformationContainer {
	/**
	 * The cached value of the '{@link #getMetaInformation() <em>Meta Information</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetaInformation()
	 * @generated
	 * @ordered
	 */
	protected EList<MetaInformation> metaInformation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetaInformationContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParallelJPackage.Literals.META_INFORMATION_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MetaInformation> getMetaInformation() {
		if (metaInformation == null) {
			metaInformation = new EObjectContainmentEList<MetaInformation>(MetaInformation.class, this, ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION);
		}
		return metaInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION:
				return ((InternalEList<?>)getMetaInformation()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION:
				return getMetaInformation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION:
				getMetaInformation().clear();
				getMetaInformation().addAll((Collection<? extends MetaInformation>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION:
				getMetaInformation().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ParallelJPackage.META_INFORMATION_CONTAINER__META_INFORMATION:
				return metaInformation != null && !metaInformation.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MetaInformationContainerImpl
