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
package me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.elements.annotated;

import static me.ezjs.generator.mybatis.api.dom.OutputUtilities.javaIndent;
import static me.ezjs.generator.mybatis.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;

import java.util.Iterator;

import me.ezjs.generator.mybatis.api.IntrospectedColumn;
import me.ezjs.generator.mybatis.api.dom.java.FullyQualifiedJavaType;
import me.ezjs.generator.mybatis.api.dom.java.Method;
import me.ezjs.generator.mybatis.codegen.mybatis3.MyBatis3FormattingUtilities;
import me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import me.ezjs.generator.mybatis.internal.util.StringUtility;
import me.ezjs.generator.mybatis.api.dom.java.Interface;

/**
 *
 * @author Jeff Butler
 */
public class AnnotatedSelectByPrimaryKeyMethodGenerator extends
        SelectByPrimaryKeyMethodGenerator {

    private boolean useResultMapIfAvailable;

    public AnnotatedSelectByPrimaryKeyMethodGenerator(boolean useResultMapIfAvailable, boolean isSimple) {
        super(isSimple);
        this.useResultMapIfAvailable = useResultMapIfAvailable;
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        method.addAnnotation("@Select({"); //$NON-NLS-1$
        javaIndent(sb, 1);
        sb.append("\"select\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        Iterator<IntrospectedColumn> iter = introspectedTable
                .getAllColumns().iterator();
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append('"');
        boolean hasColumns = false;
        while (iter.hasNext()) {
            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getSelectListPhrase(iter.next())));
            hasColumns = true;

            if (iter.hasNext()) {
                sb.append(", "); //$NON-NLS-1$
            }

            if (sb.length() > 80) {
                sb.append("\","); //$NON-NLS-1$
                method.addAnnotation(sb.toString());

                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append('"');
                hasColumns = false;
            }
        }

        if (hasColumns) {
            sb.append("\","); //$NON-NLS-1$
            method.addAnnotation(sb.toString());
        }

        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"from "); //$NON-NLS-1$
        sb.append(StringUtility.escapeStringForJava(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.setLength(0);
            javaIndent(sb, 1);
            if (and) {
                sb.append("  \"and "); //$NON-NLS-1$
            } else {
                sb.append("\"where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(StringUtility.escapeStringForJava(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }

        method.addAnnotation("})"); //$NON-NLS-1$

        if (useResultMapIfAvailable) {
            if (introspectedTable.getRules().generateBaseResultMap()
                    || introspectedTable.getRules().generateResultMapWithBLOBs()) {
                addResultMapAnnotation(interfaze, method);
            } else {
                addAnnotatedResults(interfaze, method);
            }
        } else {
            addAnnotatedResults(interfaze, method);
        }
    }

    private void addResultMapAnnotation(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap")); //$NON-NLS-1$

        String annotation = String.format("@ResultMap(\"%s.%s\")", //$NON-NLS-1$
                introspectedTable.getMyBatis3SqlMapNamespace(),
                introspectedTable.getRules().generateResultMapWithBLOBs() ?
                        introspectedTable.getResultMapWithBLOBsId() : introspectedTable.getBaseResultMapId());
        method.addAnnotation(annotation);
    }

    private void addAnnotatedResults(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$

        if (introspectedTable.isConstructorBased()) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); //$NON-NLS-1$
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); //$NON-NLS-1$
            method.addAnnotation("@ConstructorArgs({"); //$NON-NLS-1$
        } else {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); //$NON-NLS-1$
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); //$NON-NLS-1$
            method.addAnnotation("@Results({"); //$NON-NLS-1$
        }

        StringBuilder sb = new StringBuilder();

        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, true,
                    introspectedTable.isConstructorBased()));

            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }

            method.addAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, false,
                    introspectedTable.isConstructorBased()));

            if (iterNonPk.hasNext()) {
                sb.append(',');
            }

            method.addAnnotation(sb.toString());
        }

        method.addAnnotation("})"); //$NON-NLS-1$
    }
}
