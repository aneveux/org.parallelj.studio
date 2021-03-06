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
package org.parallelj.common.jdt.mergers.input;

import javax.annotation.Generated;

/**
 * EnumTestClass documentation
 * 
 */
@Generated("XA Designer")
@javax.annotation.Resource
public enum EnumTestClass {
	MERCURY(3.303e+23, 2.4397e6), VENUS(4.869e+24, 6.0518e6), EARTH(5.976e+24, 6.37814e6), MARS(
			6.421e+23, 3.3972e6), JUPITER(1.9e+27, 7.1492e7), SATURN(5.688e+26, 6.0268e7), URANUS(
			8.686e+25, 2.5559e7), NEPTUNE(1.024e+26, 2.4746e7), TEST(1.024e+26, 2.4746e7);

	/**
	 * Mass information
	 */
	@Generated("XA Designer")
	private final double mass; // in kilograms

	/**
	 * Radius information
	 */
	@Generated("XA Designer")
	private final double radius; // in meters

	/**
	 * Simple constructor
	 * 
	 * @param mass
	 * @param radius
	 */
	@Generated("XA Designer")
	EnumTestClass(double mass, double radius) throws IllegalAccessError {
		this.mass = mass;
		this.radius = radius;
	}

	protected double mass() {
		return mass;
	}

	@Generated("XA Designer")
	private double radius() {
		return radius;
	}

	@Generated("XA Designer")
	// universal gravitational constant (m3 kg-1 s-2)
	public static final double G = 6.67300E-11;

	@Generated("XA Designer")
	double surfaceGravity() {
		return G * mass / (radius * radius);
	}

	@Generated("XA Designer")
	double surfaceWeight(double otherMass) {
		return otherMass * surfaceGravity();
	}

	/**
	 * Main method used to start an enumeration test
	 * 
	 * @param args
	 */
	@Generated("XA Designer")
	public static void main(String[] args) {
		double earthWeight = Double.parseDouble(args[0]);
		double mass = earthWeight / EARTH.surfaceGravity();
		for (EnumTestClass p : EnumTestClass.values())
			System.out.printf("Your weight on %s is %f%n", p, p.surfaceWeight(mass));
	}
}
