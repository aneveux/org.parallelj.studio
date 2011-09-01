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
package org.parallelj.common.jdt.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;
import org.parallelj.common.jdt.checkers.InternalQualifiedName;

/**
 * Contains helper methods about Java code manipulation
 * 
 * @author Architecture and Methodology team
 */
public class JavaCodeHelper {

	/**
	 * Return a AbstractTypeDeclaration instance defined in a CompilationUnit
	 * with typeName as name
	 * 
	 * @param cu
	 *            A compilation unit object
	 * @param typeName
	 *            The name of the type searched
	 * @return a AbstractTypeDeclaration object associated with typeName, null
	 *         if not found or typeName null
	 */
	public static AbstractTypeDeclaration getTypeDeclaration(CompilationUnit cu, String typeName) {
		AbstractTypeDeclaration result = null;
		List types = cu.types();

		if ((typeName != null) && (types != null)) {
			Iterator typesIterator = types.iterator();

			while ((result == null) && (typesIterator.hasNext())) {
				AbstractTypeDeclaration td = (AbstractTypeDeclaration) typesIterator.next();
				result = (td.getName().getFullyQualifiedName().equals(typeName)) ? td : null;
			}
		}

		return result;
	}

	/**
	 * Return a AbstractTypeDeclaration instance defined in an other Abstract
	 * Type Declaration with innerTypeName as name
	 * 
	 * @param td
	 *            A AbstractTypeDeclaration object
	 * @param innerTypeName
	 *            The name of the inner type searched
	 * @return a AbstractTypeDeclaration object associated with innerTypeName,
	 *         null if not found or innerTypeName null
	 */
	public static AbstractTypeDeclaration getInnerTypeDeclaration(AbstractTypeDeclaration td,
			String innerTypeName) {
		AbstractTypeDeclaration result = null;
		List<AbstractTypeDeclaration> types = JavaCodeHelper.getTypedChildren(td,
				AbstractTypeDeclaration.class);
		int inc = -1;

		if ((innerTypeName != null) && (types != null)) {
			while ((result == null) && (++inc < types.size())) {
				AbstractTypeDeclaration t = types.get(inc);
				result = (t.getName().getFullyQualifiedName().equals(innerTypeName)) ? t : null;
			}
		}

		return result;
	}

	/**
	 * Return a BodyDeclaration instance according to the wanted BodyDeclaration
	 * type.
	 * 
	 * @param atd
	 *            An AbstractTypeDeclaration object
	 * @param bd
	 *            A "same Body Declaration" used to find in {@code atd}
	 * @return A BodyDeclaration instance according to the wanted
	 *         BodyDeclaration type.
	 */
	public static BodyDeclaration getBodyDeclaration(AbstractTypeDeclaration atd, BodyDeclaration bd) {
		String bdName = JavaCodeHelper.getName(bd);
		Class bdc = bd.getClass();

		if (FieldDeclaration.class.isAssignableFrom(bdc)) {
			return getField(atd, bdName);
		} else if (MethodDeclaration.class.isAssignableFrom(bdc)) {
			return getMethod(atd, bdName, ((MethodDeclaration) bd).parameters());
		} else if (AbstractTypeDeclaration.class.isAssignableFrom(bdc)) {
			return getInnerType(atd, bdName);
		} else if (EnumConstantDeclaration.class.isAssignableFrom(bdc)) {
			return getEnumConstant((EnumDeclaration) atd, bdName);
		}

		return null;
	}

	/**
	 * Return an AST FieldDeclaration object from a TypeDeclaration and a field
	 * name.
	 * 
	 * @param atd
	 *            An AbstractTypeDeclaration object
	 * @param field
	 *            A field name
	 * @return A FieldDeclaration object instance
	 */
	public static FieldDeclaration getField(AbstractTypeDeclaration atd, String fieldName) {
		List<FieldDeclaration> fds = getTypedChildren(atd, FieldDeclaration.class);

		if (fds == null) {
			return null;
		}

		boolean fieldFound = false;
		int inc = -1;

		while (!fieldFound && ++inc < fds.size()) {
			fieldFound = fieldName.equals(((VariableDeclarationFragment) fds.get(inc).fragments()
					.get(0)).getName().getFullyQualifiedName());
		}

		return (fieldFound ? fds.get(inc) : null);
	}

	/**
	 * Return all fields from a TypeDeclaration
	 * 
	 * @param td
	 *            A TypeDeclaration object
	 * @return a FieldDeclaration[]
	 */
	public static FieldDeclaration[] getFields(AbstractTypeDeclaration atd) {
		return (FieldDeclaration[]) getTypedChildren(atd, FieldDeclaration.class).toArray();
	}

	/**
	 * Return all fields names from a TypeDeclaration
	 * 
	 * @param td
	 *            A TypeDeclaration object
	 * @return a String[]
	 */
	public static String[] getFieldsNames(AbstractTypeDeclaration td) {
		FieldDeclaration[] fds = getFields(td);
		String[] fieldsNames = new String[fds.length];

		for (int inc = 0; inc < fds.length; inc++) {
			fieldsNames[inc] = getFieldName(fds[inc]);
		}

		return fieldsNames;
	}

	/**
	 * Return a name of a Body Declaration instance.
	 * 
	 * @param bd
	 *            A BodyDeclaration object
	 * @return a String containing the body declaration name
	 */
	public static String getName(BodyDeclaration bd) {
		if (FieldDeclaration.class.isAssignableFrom(bd.getClass())) {
			return getFieldName((FieldDeclaration) bd);
		} else if (MethodDeclaration.class.isAssignableFrom(bd.getClass())) {
			return getMethodName((MethodDeclaration) bd);
		} else if (AbstractTypeDeclaration.class.isAssignableFrom(bd.getClass())) {
			return getTypeName((AbstractTypeDeclaration) bd);
		} else if (EnumConstantDeclaration.class.isAssignableFrom(bd.getClass())) {
			return getEnumConstantName((EnumConstantDeclaration) bd);
		}

		return null;
	}

	/**
	 * Return a name of an ASTNode.
	 * 
	 * @param astn
	 *            A ASTNode object
	 * @return a String containing the node name
	 */
	public static String getName(ASTNode astn) {
		if (astn.getClass().isAssignableFrom(CompilationUnit.class)) {
			return getName(getMainType((CompilationUnit) astn));
		} else {
			return getName((BodyDeclaration) astn);
		}
	}

	/**
	 * Return a field name from a FieldDeclaration
	 * 
	 * @param fd
	 *            A FieldDeclaration object
	 * @return a String containif the field name
	 */
	public static String getFieldName(FieldDeclaration fd) {
		return (fd != null) ? ((VariableDeclarationFragment) fd.fragments().get(0)).getName()
				.getFullyQualifiedName() : null;
	}

	/**
	 * Return an AST MethodDeclaration object from a TypeDeclaration, a method
	 * name and a SingleVariableDeclaration list (method parameters).
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param methodName
	 *            A method name
	 * @param svds
	 *            A SingleVariableDeclaration list (method parameters)
	 * @return A MethodDeclaration object instance
	 */
	public static MethodDeclaration getMethod(AbstractTypeDeclaration atd, String methodName,
			List<SingleVariableDeclaration> svds) {
		String[] parameters = null;

		if (svds != null) {
			parameters = new String[svds.size()];

			for (int inc = 0; inc < svds.size(); inc++) {
				SingleVariableDeclaration svd = svds.get(inc);
				parameters[inc] = svd.getType().toString();
			}
		} else {
			parameters = new String[0];
		}

		return getMethod(atd, methodName, parameters);
	}

	/**
	 * Return an AST MethodDeclaration object from a TypeDeclaration and a
	 * method name. This method is useful when one and only one method with the
	 * same name appears in the type delcaration.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param methodName
	 *            A method name
	 * @return A MethodDeclaration object instance
	 */
	public static MethodDeclaration getMethod(AbstractTypeDeclaration atd, String methodName) {
		return getMethod(atd, methodName, (String[]) null);
	}

	/**
	 * Return an AST MethodDeclaration object from a TypeDeclaration, a method
	 * name and an arguments types names list.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param methodName
	 *            A method name
	 * @param argumentsTypesNames
	 *            Arguments types names as list
	 * @return A MethodDeclaration object instance
	 */
	public static MethodDeclaration getMethod(AbstractTypeDeclaration atd, String methodName,
			String... argumentsTypesNames) {
		List<MethodDeclaration> mds = getTypedChildren(atd, MethodDeclaration.class);
		boolean methodFound = false;
		int inc = -1;

		while (!methodFound && ++inc < mds.size()) {
			methodFound = methodName.equals(mds.get(inc).getName().getFullyQualifiedName());

			if (methodFound && argumentsTypesNames != null) {
				MethodDeclaration md = mds.get(inc);

				List<SingleVariableDeclaration> svds = md.parameters();

				if (svds != null && svds.size() == argumentsTypesNames.length) {
					// Check each parameter
					for (int inc2 = 0; inc2 < svds.size() && methodFound; inc2++) {
						methodFound = methodFound
								&& areTypesEquals(svds.get(inc2).getType(),
										argumentsTypesNames[inc2]);
					}
				} else {
					methodFound = false;
				}
			}
		}

		return (methodFound ? mds.get(inc) : null);
	}

	/**
	 * Return a List of AST MethodDeclaration object from a TypeDeclaration and a method
	 * name.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param methodName
	 *            A method name
	 * @return A List<MethodDeclaration> object instance
	 */
	public static List<MethodDeclaration>  getMethods(AbstractTypeDeclaration atd, String methodName) {
		return getMethods(atd, methodName, (String[]) null);
	}

	/**
	 * Return an List of AST MethodDeclaration object from a TypeDeclaration, a method
	 * name and an arguments types names list.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param methodName
	 *            A method name
	 * @param argumentsTypesNames
	 *            Arguments types names as list
	 * @return A List<MethodDeclaration> object instance
	 */
	public static List<MethodDeclaration> getMethods(AbstractTypeDeclaration atd, String methodName,
			String... argumentsTypesNames) {
		List<MethodDeclaration> mds = (List<MethodDeclaration>)getTypedChildren(atd, MethodDeclaration.class);
		int inc = -1;
		List<MethodDeclaration> lstMethods = new ArrayList<MethodDeclaration>();

		while (++inc < mds.size()) {
			boolean methodFound = false;
			methodFound = methodName.equals(mds.get(inc).getName().getFullyQualifiedName());

			MethodDeclaration md = mds.get(inc);
			if (methodFound && argumentsTypesNames != null) {
				List<SingleVariableDeclaration> svds = md.parameters();

				if (svds != null && svds.size() == argumentsTypesNames.length) {
					// Check each parameter
					for (int inc2 = 0; inc2 < svds.size() && methodFound; inc2++) {
						methodFound = methodFound
								&& areTypesEquals(svds.get(inc2).getType(),
										argumentsTypesNames[inc2]);
					}
				}
			}
			if (methodFound)
				lstMethods.add(md);
		}

		return lstMethods;
	}
	
	/**
	 * Return an AST TypeDeclaration object from a TypeDeclaration and a inner
	 * type name.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration object
	 * @param innerTypeName
	 *            An inner type name
	 * @return A AbstractTypeDeclaration object instance
	 */
	public static AbstractTypeDeclaration getInnerType(AbstractTypeDeclaration atd,
			String innerTypeName) {
		List<AbstractTypeDeclaration> atds = getTypedChildren(atd, AbstractTypeDeclaration.class);
		boolean innerTypeFound = false;
		int inc = -1;

		while (!innerTypeFound && ++inc < atds.size()) {
			innerTypeFound = innerTypeName.equals(atds.get(inc).getName().getFullyQualifiedName());
		}

		return (innerTypeFound ? atds.get(inc) : null);
	}

	/**
	 * Return an AST AbstractTypeDeclaration object from a CompilationUnit and a
	 * type name.
	 * 
	 * @param cu
	 *            A CompilationUnit object
	 * @param typeName
	 *            An type name
	 * @return A AbstractTypeDeclaration object instance
	 */
	public static AbstractTypeDeclaration getType(CompilationUnit cu, String typeName) {
		if (cu == null || typeName == null) {
			return null;
		}

		List tds = cu.types();
		boolean typeFound = false;
		int inc = -1;

		while (!typeFound && ++inc < tds.size()) {
			typeFound = typeName.equals(getTypeName((AbstractTypeDeclaration) tds.get(inc)));
		}

		return (typeFound ? (AbstractTypeDeclaration) tds.get(inc) : null);
	}

	/**
	 * Return the name of the type describes in a TypeDeclaration instance.
	 * 
	 * @param atd
	 *            A AbstractTypeDeclaration instance
	 * @param The
	 *            name of the type describes by TypeDeclaration
	 */
	public static String getTypeName(AbstractTypeDeclaration atd) {
		return atd.getName().getFullyQualifiedName();
	}

	/**
	 * Return an AST AbstractTypeDeclaration object from a CompilationUnit.
	 * 
	 * @param cu
	 *            A CompilationUnit object
	 * @return A AbstractTypeDeclaration object instance
	 */
	public static AbstractTypeDeclaration getMainType(CompilationUnit cu) {
		if (cu == null) {
			return null;
		}

		List<AbstractTypeDeclaration> tds = cu.types();
		boolean typeFound = false;
		int inc = -1;

		while (!typeFound && ++inc < tds.size()) {
			typeFound = Modifier.isPublic(tds.get(inc).getModifiers());
		}

		return (typeFound ? (AbstractTypeDeclaration) tds.get(inc) : null);
	}

	/**
	 * Return all methods from a TypeDeclaration
	 * 
	 * @param td
	 *            A TypeDeclaration object
	 * @return a MethodDeclaration[]
	 */
	public static MethodDeclaration[] getMethods(AbstractTypeDeclaration atd) {
		return (MethodDeclaration[]) getTypedChildren(atd, MethodDeclaration.class).toArray();
	}

	/**
	 * Return all methods names from a TypeDeclaration
	 * 
	 * @param td
	 *            A TypeDeclaration object
	 * @return a String[]
	 */
	public static String[] getMethodsNames(AbstractTypeDeclaration td) {
		MethodDeclaration[] mds = getMethods(td);
		String[] methodsNames = new String[mds.length];

		for (int inc = 0; inc < mds.length; inc++) {
			methodsNames[inc] = getMethodName(mds[inc]);
		}

		return methodsNames;
	}

	/**
	 * Return a method name from a MethodDeclaration
	 * 
	 * @param md
	 *            A MethodDeclaration object
	 * @return a String containing the method name
	 */
	public static String getMethodName(MethodDeclaration md) {
		return (md != null) ? md.getName().getFullyQualifiedName() : null;
	}
	
	

	/**
	 * Return the initialization expression of a field.
	 */
	public static Expression getFieldInitializer(FieldDeclaration fd) {
		List<VariableDeclarationFragment> vdfs = fd.fragments();

		return vdfs.get(0).getInitializer();
	}

	/**
	 * Return an ImportDeclaration of a CompilationUnit
	 */
	public static ImportDeclaration getImport(CompilationUnit cu, String importName) {
		List imports = cu.imports();
		List<String> importsNames = new ArrayList<String>();

		for (Object o : imports) {
			importsNames.add(((ImportDeclaration) o).getName().getFullyQualifiedName());
		}

		return (importsNames.indexOf(importName) == -1) ? null : (ImportDeclaration) imports
				.get(importsNames.indexOf(importName));
	}

	/**
	 * Return an Annotation object of a BodyDeclaration object
	 */
	public static Annotation getAnnotation(BodyDeclaration bd, String annotationFullyQualifiedName) {
		List modifiers = bd.modifiers();
		Annotation result = null;

		// Test if this BodyDeclaration contains modifiers
		if (modifiers != null) {
			Iterator modifiersIterator = bd.modifiers().iterator();

			// For each modifier, search for @annotationType marker
			// annotation
			while ((result == null) && modifiersIterator.hasNext()) {
				IExtendedModifier modifier = (IExtendedModifier) modifiersIterator.next();

				if (modifier.isAnnotation()) {
					Annotation a = (Annotation) modifier;

					// Test if the name of the annotation is a simple name or a
					// fully qualified name
					String annotationName = (a.getTypeName().isSimpleName()) ? annotationFullyQualifiedName
							.substring(annotationFullyQualifiedName.lastIndexOf('.') + 1)
							: annotationFullyQualifiedName;

					if (a.getTypeName().getFullyQualifiedName().equals(annotationName)) {
						result = a;
					}
				}
			}
		}

		return result;
	}
	
	/**
	 * Return an Annotation object of a SingleVariableDeclaration object
	 */
	public static Annotation getAnnotation(SingleVariableDeclaration svd, String annotationFullyQualifiedName) {
		List modifiers = svd.modifiers();
		Annotation result = null;

		// Test if this BodyDeclaration contains modifiers
		if (modifiers != null) {
			Iterator modifiersIterator = svd.modifiers().iterator();

			// For each modifier, search for @annotationType marker
			// annotation
			while ((result == null) && modifiersIterator.hasNext()) {
				IExtendedModifier modifier = (IExtendedModifier) modifiersIterator.next();

				if (modifier.isAnnotation()) {
					Annotation a = (Annotation) modifier;

					// Test if the name of the annotation is a simple name or a
					// fully qualified name
					String annotationName = (a.getTypeName().isSimpleName()) ? annotationFullyQualifiedName
							.substring(annotationFullyQualifiedName.lastIndexOf('.') + 1)
							: annotationFullyQualifiedName;

					if (a.getTypeName().getFullyQualifiedName().equals(annotationName)) {
						result = a;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Return true if the annotation content matches the input map
	 * (@Foo(f1 = v1 , f2 = v2)
	 * 
	 * @param annotation input annotation to check
	 * @param content a Map object containing as key the expected member name and as
	 * value the expected member value
	 * @return true if the annotation is a normal annotation and if the content matches the content parameter, 
	 * false otherwise
	 */
	public static boolean checkAnnotationContent(Annotation annotation, Map<String, Object> content) {
		boolean correct = false;

		// Test if this annotation is a Normal Member Annotation
		if (annotation != null && annotation.isNormalAnnotation()) {
			List<MemberValuePair> values = ((NormalAnnotation) annotation).values();
			correct = true;
	
			for (int inc = 0; inc < values.size() && correct; inc++) {
				MemberValuePair mvp = values.get(inc);
				String memberName = mvp.getName().getFullyQualifiedName();
				Object contentValue = content.get(memberName);
				correct = contentValue != null;
	
				Expression memberValue = mvp.getValue();
				
				correct = checkSingleAnnotationValue(memberValue, contentValue);
			}
		}
		return correct;
	}
	
	/**
	 * Compare two annotations contents
	 * @param memberValue
	 * @param contentValue
	 * @return true if content matches, false else
	 */
	public static boolean checkSingleAnnotationValue(Expression memberValue, Object contentValue) {
		
		boolean correct = false;
		
		if (memberValue != null) {
			if (contentValue instanceof String) {
				correct = ((memberValue instanceof StringLiteral) && (contentValue
						.equals(((StringLiteral) memberValue).getLiteralValue())));
			} else {
				if(contentValue instanceof InternalQualifiedName) {
					if(!(memberValue instanceof QualifiedName)) {
						correct = false;
					} else {
						InternalQualifiedName internalQualifiedName = (InternalQualifiedName)contentValue;
						correct = ((QualifiedName) memberValue).getQualifier().getFullyQualifiedName().equals(internalQualifiedName.getQualifier()) &&
								((QualifiedName) memberValue).getName().getFullyQualifiedName().equals(internalQualifiedName.getName());
					}							
				}
			}
		}
		return correct;
	}

	/**
	 * Return true if {@code firstType} as Type object is equals to
	 * {@code secondType} as Type
	 * 
	 * @param type1
	 *            first type as Type object
	 * @param type2
	 *            second type as Type object
	 * @return true if {@code firstType} is equals to {@code secondType}
	 */
	public static boolean areTypesEquals(Type type1, Type type2) {
		return areTypesEquals(type1, type2.toString());
	}

	/**
	 * Return true if {@code firstType} as Type object is equals to
	 * {@code secondType} as fully qualified String
	 * 
	 * @param type1
	 *            first type as Type object
	 * @param type2
	 *            second type as fully qualified String
	 * @return true if {@code firstType} is equals to {@code secondType}
	 */
	public static boolean areTypesEquals(Type type1, String type2) {

		// Check if type1 and type2 are not null before method
		// execution
		if (type1 == null || type2 == null) {
			return false;
		}

		type2 = type2.trim();

		/* Remove the generic information from type 2 is this one exists */
		type2 = (type2.indexOf('<') != -1) ? type2.substring(0, type2.indexOf('<')) : type2;

		if (type1 instanceof SimpleType) {
			// Case when type1 is a simple type (MyObject)
			String type1Name = ((SimpleType) type1).getName().getFullyQualifiedName();
			String simpleType1Name = (type1Name.lastIndexOf('.') != -1) ? type1Name.substring(type1Name
					.lastIndexOf('.') + 1) : type1Name;
			String simpleType2Name = (type2.lastIndexOf('.') != -1) ? type2.substring(type2
					.lastIndexOf('.') + 1) : type2;

			return simpleType1Name.equals(simpleType2Name);
		} else if (type1 instanceof QualifiedType) {
			// Case when type1 is a qualified type (MyObject)
			return (((QualifiedType) type1).getName().getFullyQualifiedName().equals(type2));
		} else if (type1 instanceof PrimitiveType) {
			// Case when type1 is a primitive type (int, boolean, void, ...)
			return ((PrimitiveType) type1).getPrimitiveTypeCode().toString()
					.equals(type2);
		} else if (type1 instanceof ArrayType) {
			// Case when type1 is an array (int[], MyObject[],...)
			return type2.endsWith("[]")
					&& areTypesEquals(((ArrayType) type1).getComponentType(), type2.substring(0,
							type2.lastIndexOf('[')));
		} else if (type1 instanceof ParameterizedType) {
			// Case when type1 is a parametrized type (MyObject<Integer>,
			// Map<List,String>,...)

			// Check the main type
			return areTypesEquals(((ParameterizedType) type1).getType(), type2);
		} else if (type1 instanceof WildcardType) {
			// Case when type1 is a wildcard type (? , ? extends MyObject,
			// ...)
			if (((WildcardType) type1).getBound() == null) {
				return type2.equals("?");
			} else {
				StringTokenizer st = new StringTokenizer(type2, " ");

				if (!st.nextToken().trim().equals("?")) {
					return false;
				}

				if (((WildcardType) type1).isUpperBound()) {
					return st.nextToken().trim().equalsIgnoreCase("extends")
							&& areTypesEquals(((WildcardType) type1).getBound(), st.nextToken());
				} else {
					return st.nextToken().trim().equalsIgnoreCase("super")
							&& areTypesEquals(((WildcardType) type1).getBound(), st.nextToken());
				}
			}
		}

		return false;
	}

	/**
	 * Return a simple name from a fully qualified name. If this name is already
	 * a simple name (without dot), this method returns the original name. If
	 * this name is a fully qualified name, this method return the class name
	 * without dot.
	 * 
	 * @param fullyQualifiedName
	 *            The fully qualified name to transform
	 * @return a simple name from a fully qualified name. If this name is
	 *         already a simple name (without dot), this method returns the
	 *         original name. If this name is a fully qualified name, this
	 *         method return the class name without dot.
	 */
	public static String getSimpleName(String fullyQualifiedName) {
		if (fullyQualifiedName == null) {
			return null;
		}

		// Check if fullyQualifiedName contains a dot
		if (fullyQualifiedName.lastIndexOf('.') == -1) {
			return fullyQualifiedName;
		} else {
			return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);
		}
	}

	/**
	 * Return a list of {@code typeWanted} typed elements as children of
	 * {@code atd} AbstractTypeDeclaration.
	 * 
	 * @param atd
	 *            The parent instance
	 * @param typeWanted
	 *            Type of children wanted
	 * @return a list of {@code typeWanted} typed elements as children of
	 *         {@code atd} AbstractTypeDeclaration
	 */
	public static List getTypedChildren(AbstractTypeDeclaration atd, Class typeWanted) {
		if (atd == null) {
			return null;
		}

		List<BodyDeclaration> result = new ArrayList<BodyDeclaration>(5);

		if (typeWanted.equals(EnumConstantDeclaration.class)) {
			result = ((EnumDeclaration) atd).enumConstants();
		} else {
			for (BodyDeclaration bd : (List<BodyDeclaration>) atd.bodyDeclarations()) {
				if (typeWanted.isAssignableFrom(bd.getClass())) {
					result.add(bd);
				}
			}
		}

		return result;
	}

	/**
	 * Return the name of the enum constant describes in a
	 * EnumConstantDeclaration instance.
	 * 
	 * @param ecd
	 *            A EnumConstantDeclaration instance
	 * @param The
	 *            name of the enum constant describes by EnumConstantDeclaration
	 */
	public static String getEnumConstantName(EnumConstantDeclaration ecd) {
		return ecd.getName().getFullyQualifiedName();
	}

	/**
	 * Return an AST EnumConstantDeclaration object from a EnumDeclaration and a
	 * enumeration constant name.
	 * 
	 * @param ed
	 *            An EnumDeclaration object
	 * @param enumerationConstantName
	 *            An enumeration constant name
	 * @return A EnumConstantDeclaration object instance
	 */
	public static EnumConstantDeclaration getEnumConstant(EnumDeclaration ed,
			String enumerationConstantName) {
		List<EnumConstantDeclaration> atds = ed.enumConstants();
		boolean enumerationConstantNameFound = false;
		int inc = -1;

		while (!enumerationConstantNameFound && ++inc < atds.size()) {
			enumerationConstantNameFound = enumerationConstantName.equals(atds.get(inc).getName()
					.getFullyQualifiedName());
		}

		return (enumerationConstantNameFound ? atds.get(inc) : null);
	}

	/**
	 * Return a Body Declaration Property according to the {@code type} type.
	 * 
	 * @param type
	 *            A Body Declaration type
	 * @return a Body Declaration Property according to the {@code type} type.
	 */
	public static ChildListPropertyDescriptor getBodyDeclarationProperty(Class type) {
		if (type.equals(TypeDeclaration.class)) {
			return TypeDeclaration.BODY_DECLARATIONS_PROPERTY;
		} else if (type.equals(EnumDeclaration.class)) {
			return EnumDeclaration.BODY_DECLARATIONS_PROPERTY;
		} else if (type.equals(AnnotationTypeDeclaration.class)) {
			return AnnotationTypeDeclaration.BODY_DECLARATIONS_PROPERTY;
		}

		return null;
	}

	/**
	 * Return the simple name as String from a Name instance
	 */
	public static String getSimpleName(Name n) {
		String simpleName = null;

		/*
		 * If it's a fully qualified name, remove package name.
		 */
		if (n.isQualifiedName()) {
			simpleName = n.getFullyQualifiedName().substring(
					n.getFullyQualifiedName().lastIndexOf('.') + 1);
		} else {
			simpleName = n.getFullyQualifiedName();

		}

		return simpleName;
	}

	/**
	 * Return a name instance from a Type instance
	 */
	public static Name getName(Type t) {
		if (t.isArrayType()) {
			return getName(((ArrayType) t).getComponentType());
		} else if (t.isParameterizedType()) {
			return getName(((ParameterizedType) t).getType());
		} else if (t.isPrimitiveType()) {
			return null;
		} else if (t.isQualifiedType()) {
			return ((QualifiedType) t).getName();
		} else if (t.isSimpleType()) {
			return ((SimpleType) t).getName();
		}

		return null;
	}
}
