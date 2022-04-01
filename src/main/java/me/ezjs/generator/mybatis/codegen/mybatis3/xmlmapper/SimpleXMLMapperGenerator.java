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
package me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper;

import static me.ezjs.generator.mybatis.internal.util.messages.Messages.getString;

import me.ezjs.generator.mybatis.api.FullyQualifiedTable;
import me.ezjs.generator.mybatis.api.dom.xml.Attribute;
import me.ezjs.generator.mybatis.api.dom.xml.Document;
import me.ezjs.generator.mybatis.codegen.XmlConstants;
import me.ezjs.generator.mybatis.internal.util.messages.Messages;
import me.ezjs.generator.mybatis.api.dom.xml.XmlElement;
import me.ezjs.generator.mybatis.codegen.AbstractXmlGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.SimpleSelectAllElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.SimpleSelectByPrimaryKeyElementGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

/**
 *
 * @author Jeff Butler
 *
 */
public class SimpleXMLMapperGenerator extends AbstractXmlGenerator {

    public SimpleXMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(Messages.getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        context.getCommentGenerator().addRootComment(answer);

        addResultMapElement(answer);
        addDeleteByPrimaryKeyElement(answer);
        addInsertElement(answer);
        addUpdateByPrimaryKeyElement(answer);
        addSelectByPrimaryKeyElement(answer);
        addSelectAllElement(answer);

        return answer;
    }

    protected void addResultMapElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(
                    true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new SimpleSelectByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SimpleSelectAllElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator(true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator(true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator(
                    true);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void initializeAndExecuteGenerator(
            AbstractXmlElementGenerator elementGenerator,
            XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document,
                introspectedTable)) {
            document = null;
        }

        return document;
    }
}
