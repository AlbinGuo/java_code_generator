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
package me.ezjs.generator.mybatis.codegen.ibatis2.model;

import static me.ezjs.generator.mybatis.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import me.ezjs.generator.mybatis.api.CommentGenerator;
import me.ezjs.generator.mybatis.api.FullyQualifiedTable;
import me.ezjs.generator.mybatis.api.IntrospectedColumn;
import me.ezjs.generator.mybatis.api.Plugin;
import me.ezjs.generator.mybatis.codegen.RootClassInfo;
import me.ezjs.generator.mybatis.internal.util.JavaBeansUtil;
import me.ezjs.generator.mybatis.internal.util.messages.Messages;
import me.ezjs.generator.mybatis.api.dom.java.CompilationUnit;
import me.ezjs.generator.mybatis.api.dom.java.Field;
import me.ezjs.generator.mybatis.api.dom.java.JavaVisibility;
import me.ezjs.generator.mybatis.api.dom.java.Method;
import me.ezjs.generator.mybatis.api.dom.java.TopLevelClass;
import me.ezjs.generator.mybatis.codegen.AbstractJavaGenerator;

/**
 *
 * @author Jeff Butler
 *
 */
public class RecordWithBLOBsGenerator extends AbstractJavaGenerator {

    public RecordWithBLOBsGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(Messages.getString(
                "Progress.9", table.toString())); //$NON-NLS-1$
        Plugin plugins = context.getPlugins();
        CommentGenerator commentGenerator = context.getCommentGenerator();

        TopLevelClass topLevelClass = new TopLevelClass(introspectedTable
                .getRecordWithBLOBsType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        if (introspectedTable.getRules().generateBaseRecordClass()) {
            topLevelClass.setSuperClass(introspectedTable.getBaseRecordType());
        } else {
            topLevelClass.setSuperClass(introspectedTable.getPrimaryKeyType());
        }

        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getBLOBColumns()) {
            if (RootClassInfo.getInstance(rootClass, warnings)
                    .containsProperty(introspectedColumn)) {
                continue;
            }

            Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }

            Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addMethod(method);
            }

            method = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelSetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.RECORD_WITH_BLOBS)) {
                topLevelClass.addMethod(method);
            }
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelRecordWithBLOBsClassGenerated(
                topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
}
