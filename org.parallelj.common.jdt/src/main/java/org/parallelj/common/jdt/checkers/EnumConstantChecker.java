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
package org.parallelj.common.jdt.checkers;

import org.eclipse.jdt.core.dom.EnumConstantDeclaration;

/**
 * Defines several methods used to perform Java enum constant checking.
 * 
 * @author Atos Worldline
 */
public class EnumConstantChecker extends ElementChecker {

	private EnumConstantDeclaration ecd = null;

	/**
	 * Construct a EnumConstantChecker with a EnumConstantDeclaration as single
	 * parameter.
	 * 
	 * @param fd
	 *            A JDT EnumConstantDeclaration object
	 * @throws IllegalArgumentException
	 *             If {@code ecd} is null
	 */
	EnumConstantChecker(EnumConstantDeclaration ecd) {
		super(ecd);

		if (ecd == null) {
			throw new IllegalArgumentException(
					"Cannot create a EnumConstantChecker with a null EnumConstantDeclaration");
		}

		this.ecd = ecd;
	}

	/**
	 * Return true if this constant contains anonymous class declaration.
	 * 
	 * @return true if this constant contains anonymous class declaration.
	 */
	public boolean containsAnonymousClassDeclaration() {
		return (this.ecd.getAnonymousClassDeclaration() != null);
	}
}
