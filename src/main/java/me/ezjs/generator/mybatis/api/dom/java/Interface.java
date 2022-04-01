/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.ezjs.generator.mybatis.api.dom.java;

import me.ezjs.generator.mybatis.api.dom.OutputUtilities;
import me.ezjs.generator.mybatis.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Class Interface.
 *
 * @author Jeff Butler
 */
public class Interface extends JavaElement implements CompilationUnit {

    /** The imported types. */
    private Set<FullyQualifiedJavaType> importedTypes;

    /** The static imports. */
    private Set<String> staticImports;

    /** The type. */
    private FullyQualifiedJavaType type;

    /** The super interface types. */
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    /** The methods. */
    private List<Method> methods;

    /** The file comment lines. */
    private List<String> fileCommentLines;

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public Interface(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        superInterfaceTypes = new LinkedHashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        importedTypes = new TreeSet<FullyQualifiedJavaType>();
        fileCommentLines = new ArrayList<String>();
        staticImports = new TreeSet<String>();
    }

    /**
     * Instantiates a new interface.
     *
     * @param type
     *            the type
     */
    public Interface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getImportedTypes()
     */
    public Set<FullyQualifiedJavaType> getImportedTypes() {
        return Collections.unmodifiableSet(importedTypes);
    }

    /* (non-Javadoc)
     * @see CompilationUnit#addImportedType(FullyQualifiedJavaType)
     */
    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType.isExplicitlyImported()
                && !importedType.getPackageName().equals(type.getPackageName())) {
            importedTypes.add(importedType);
        }
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getFormattedContent()
     */
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        for (String commentLine : fileCommentLines) {
            sb.append(commentLine);
            OutputUtilities.newLine(sb);
        }

        if (StringUtility.stringHasValue(getType().getPackageName())) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(getType().getPackageName());
            sb.append(';');
            OutputUtilities.newLine(sb);
            OutputUtilities.newLine(sb);
        }

        for (String staticImport : staticImports) {
            sb.append("import static "); //$NON-NLS-1$
            sb.append(staticImport);
            sb.append(';');
            OutputUtilities.newLine(sb);
        }

        if (staticImports.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Set<String> importStrings = OutputUtilities.calculateImports(importedTypes);
        for (String importString : importStrings) {
            sb.append(importString);
            OutputUtilities.newLine(sb);
        }

        if (importStrings.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        int indentLevel = 0;

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        sb.append(getVisibility().getValue());

        if (isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("interface "); //$NON-NLS-1$
        sb.append(getType().getShortName());

        if (getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends "); //$NON-NLS-1$

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : getSuperInterfaceTypes()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(JavaDomUtils.calculateTypeName(this, fqjt));
            }
        }

        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        Iterator<Method> mtdIter = getMethods().iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, true, this));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    /**
     * Adds the super interface.
     *
     * @param superInterface
     *            the super interface
     */
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    /**
     * Gets the methods.
     *
     * @return Returns the methods.
     */
    public List<Method> getMethods() {
        return methods;
    }

    /**
     * Adds the method.
     *
     * @param method
     *            the method
     */
    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * Gets the type.
     *
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getSuperClass()
     */
    public FullyQualifiedJavaType getSuperClass() {
        // interfaces do not have superclasses
        return null;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getSuperInterfaceTypes()
     */
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#isJavaInterface()
     */
    public boolean isJavaInterface() {
        return true;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#isJavaEnumeration()
     */
    public boolean isJavaEnumeration() {
        return false;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#addFileCommentLine(java.lang.String)
     */
    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getFileCommentLines()
     */
    public List<String> getFileCommentLines() {
        return fileCommentLines;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#addImportedTypes(java.util.Set)
     */
    public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
        this.importedTypes.addAll(importedTypes);
    }

    /* (non-Javadoc)
     * @see CompilationUnit#getStaticImports()
     */
    public Set<String> getStaticImports() {
        return staticImports;
    }

    /* (non-Javadoc)
     * @see CompilationUnit#addStaticImport(java.lang.String)
     */
    public void addStaticImport(String staticImport) {
        staticImports.add(staticImport);
    }

    /* (non-Javadoc)
     * @see CompilationUnit#addStaticImports(java.util.Set)
     */
    public void addStaticImports(Set<String> staticImports) {
        this.staticImports.addAll(staticImports);
    }
}
