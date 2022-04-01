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
import static me.ezjs.generator.mybatis.internal.util.StringUtility.escapeStringForJava;

import java.util.Iterator;

import me.ezjs.generator.mybatis.api.IntrospectedColumn;
import me.ezjs.generator.mybatis.api.dom.java.FullyQualifiedJavaType;
import me.ezjs.generator.mybatis.api.dom.java.Method;
import me.ezjs.generator.mybatis.codegen.mybatis3.MyBatis3FormattingUtilities;
import me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import me.ezjs.generator.mybatis.api.dom.java.Interface;

/**
 *
 * @author Jeff Butler
 */
public class AnnotatedDeleteByPrimaryKeyMethodGenerator extends
        DeleteByPrimaryKeyMethodGenerator {

    public AnnotatedDeleteByPrimaryKeyMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Delete")); //$NON-NLS-1$

        method.addAnnotation("@Delete({"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"delete from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(
                introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        boolean and = false;
        Iterator<IntrospectedColumn> iter = introspectedTable.getPrimaryKeyColumns().iterator();
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

            sb.append(escapeStringForJava(
                    MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }

            method.addAnnotation(sb.toString());
        }

        method.addAnnotation("})"); //$NON-NLS-1$
    }
}
