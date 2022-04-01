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
package me.ezjs.generator.mybatis.codegen.ibatis2.sqlmap.elements;

import me.ezjs.generator.mybatis.api.IntrospectedColumn;
import me.ezjs.generator.mybatis.api.dom.xml.Attribute;
import me.ezjs.generator.mybatis.api.dom.xml.XmlElement;
import me.ezjs.generator.mybatis.codegen.AbstractGenerator;
import me.ezjs.generator.mybatis.config.GeneratedKey;
import me.ezjs.generator.mybatis.internal.util.StringUtility;
import me.ezjs.generator.mybatis.api.dom.xml.TextElement;

/**
 *
 * @author Jeff Butler
 *
 */
public abstract class AbstractXmlElementGenerator extends AbstractGenerator {
    public abstract void addElements(XmlElement parentElement);

    public AbstractXmlElementGenerator() {
        super();
    }

    /**
     * This method should return an XmlElement for the select key used to
     * automatically generate keys.
     *
     * @param introspectedColumn
     *            the column related to the select key statement
     * @param generatedKey
     *            the generated key for the current table
     * @return the selectKey element
     */
    protected XmlElement getSelectKey(IntrospectedColumn introspectedColumn,
                                      GeneratedKey generatedKey) {
        String identityColumnType = introspectedColumn
                .getFullyQualifiedJavaType().getFullyQualifiedName();

        XmlElement answer = new XmlElement("selectKey"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultClass", identityColumnType)); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
        if (StringUtility.stringHasValue(generatedKey.getType())) {
            answer.addAttribute(new Attribute("type", generatedKey.getType())); //$NON-NLS-1$  
        }
        answer
                .addElement(new TextElement(generatedKey
                        .getRuntimeSqlStatement()));

        return answer;
    }

    protected XmlElement getBaseColumnListElement() {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getBaseColumnListId())); //$NON-NLS-1$
        return answer;
    }

    protected XmlElement getBlobColumnListElement() {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid", //$NON-NLS-1$
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getBlobColumnListId())); //$NON-NLS-1$
        return answer;
    }
}
