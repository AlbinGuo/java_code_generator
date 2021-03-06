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
package me.ezjs.generator.mybatis;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import me.ezjs.generator.mybatis.api.MyBatisGenerator;
import me.ezjs.generator.mybatis.config.Configuration;
import me.ezjs.generator.mybatis.config.xml.ConfigurationParser;
import me.ezjs.generator.mybatis.internal.DefaultShellCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import me.ezjs.generator.mybatis.api.GeneratedXmlFile;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@RunWith(Parameterized.class)
public class XmlCodeGenerationTest {

    private GeneratedXmlFile generatedXmlFile;

    public XmlCodeGenerationTest(GeneratedXmlFile generatedXmlFile) {
        this.generatedXmlFile = generatedXmlFile;
    }

    @Test
    public void testXmlParse() {
        ByteArrayInputStream is = new ByteArrayInputStream(
                generatedXmlFile.getFormattedContent().getBytes());
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new TestEntityResolver());
            builder.setErrorHandler(new TestErrorHandler());
            builder.parse(is);
        } catch (Exception e) {
            fail("Generated XML File " + generatedXmlFile.getFileName() + " will not parse");
        }
    }

    @Parameters
    public static List<GeneratedXmlFile> generateXmlFiles() throws Exception {
        List<GeneratedXmlFile> generatedFiles = new ArrayList<GeneratedXmlFile>();
        generatedFiles.addAll(generateXmlFilesMybatis());
        generatedFiles.addAll(generateXmlFilesIbatis());
        return generatedFiles;
    }

    private static List<GeneratedXmlFile> generateXmlFilesMybatis() throws Exception {
        JavaCodeGenerationTest.createDatabase();
        return generateXmlFiles("/scripts/generatorConfig.xml");
    }

    private static List<GeneratedXmlFile> generateXmlFilesIbatis() throws Exception {
        JavaCodeGenerationTest.createDatabase();
        return generateXmlFiles("/scripts/ibatorConfig.xml");
    }

    private static List<GeneratedXmlFile> generateXmlFiles(String configFile) throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(JavaCodeGenerationTest.class.getResourceAsStream(configFile));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator.getGeneratedXmlFiles();
    }

    public static class TestEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            // just return an empty string.  this should stop the parser from trying to access the network
            return new InputSource(new ByteArrayInputStream("".getBytes()));
        }
    }

    public static class TestErrorHandler implements ErrorHandler {

        private List<String> errors = new ArrayList<String>();
        private List<String> warnings = new ArrayList<String>();

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            warnings.add(exception.getMessage());
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            errors.add(exception.getMessage());
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            errors.add(exception.getMessage());
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }
    }
}
