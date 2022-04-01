/**
 * Copyright 2006-2016 the original author or authors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.ezjs.generator.plugins;

import me.ezjs.generator.mybatis.api.IntrospectedTable;
import me.ezjs.generator.mybatis.api.PluginAdapter;
import me.ezjs.generator.mybatis.api.dom.java.*;

import java.util.List;
import java.util.Properties;

public class ModelCustomizedPlugin extends PluginAdapter {

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addTableNameAnnotation(introspectedTable, topLevelClass);
        addExtendsRootObject(introspectedTable, topLevelClass);
        return true;
    }

    private void addExtendsRootObject(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {

        FullyQualifiedJavaType superClass = topLevelClass.getSuperClass();
        if (superClass == null) {
            topLevelClass.setSuperClass("RootObject");
            topLevelClass.addImportedType(new FullyQualifiedJavaType("me.ezjs.core.model.RootObject"));

            // remove id, createTime, modifyTime
        }
    }

    private void addTableNameAnnotation(IntrospectedTable introspectedTable,
                                        TopLevelClass topLevelClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("@TableName(");
        sb.append("\"" + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + "\"");
        sb.append(")");
        topLevelClass.addAnnotation(sb.toString());


        topLevelClass.addImportedType(new FullyQualifiedJavaType("me.ezjs.core.model.TableName"));

    }
}
