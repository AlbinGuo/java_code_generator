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
package me.ezjs.generator.mybatis.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import me.ezjs.generator.mybatis.api.GeneratedJavaFile;
import me.ezjs.generator.mybatis.api.GeneratedXmlFile;
import me.ezjs.generator.mybatis.api.IntrospectedTable;
import me.ezjs.generator.mybatis.api.ProgressCallback;
import me.ezjs.generator.mybatis.api.dom.java.CompilationUnit;
import me.ezjs.generator.mybatis.api.dom.xml.Document;
import me.ezjs.generator.mybatis.codegen.AbstractGenerator;
import me.ezjs.generator.mybatis.codegen.AbstractJavaClientGenerator;
import me.ezjs.generator.mybatis.codegen.AbstractJavaGenerator;
import me.ezjs.generator.mybatis.codegen.AbstractXmlGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.JavaMapperGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.javamapper.MixedClientGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.model.BaseRecordGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.model.ExampleGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.model.PrimaryKeyGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import me.ezjs.generator.mybatis.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import me.ezjs.generator.mybatis.config.PropertyRegistry;
import me.ezjs.generator.mybatis.internal.ObjectFactory;
import me.ezjs.generator.plugins.EzjsMapperGenerator;

/**
 * The Class IntrospectedTableMyBatis3Impl.
 *
 * @author Jeff Butler
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {

    /** The java model generators. */
    protected List<AbstractJavaGenerator> javaModelGenerators;

    /** The client generators. */
    protected List<AbstractJavaGenerator> clientGenerators;

    /** The xml mapper generator. */
    protected AbstractXmlGenerator xmlMapperGenerator;

    /**
     * Instantiates a new introspected table my batis3 impl.
     */
    public IntrospectedTableMyBatis3Impl() {
        super(TargetRuntime.MYBATIS3);
        javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        clientGenerators = new ArrayList<AbstractJavaGenerator>();
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#calculateGenerators(java.util.List, ProgressCallback)
     */
    @Override
    public void calculateGenerators(List<String> warnings,
                                    ProgressCallback progressCallback) {
        calculateJavaModelGenerators(warnings, progressCallback);

        AbstractJavaClientGenerator javaClientGenerator =
                calculateClientGenerators(warnings, progressCallback);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    /**
     * Calculate xml mapper generator.
     *
     * @param javaClientGenerator
     *            the java client generator
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator,
                                               List<String> warnings,
                                               ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }

        initializeAbstractGenerator(xmlMapperGenerator, warnings,
                progressCallback);
    }

    /**
     * Calculate client generators.
     *
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     * @return true if an XML generator is required
     */
    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings,
                                                                    ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        clientGenerators.add(javaGenerator);

        return javaGenerator;
    }

    /**
     * Creates the java client generator.
     *
     * @return the abstract java client generator
     */
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration()
                .getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new MixedClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory
                    .createInternalObject(type);
        }

        return javaGenerator;
    }

    /**
     * Calculate java model generators.
     *
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    protected void calculateJavaModelGenerators(List<String> warnings,
                                                ProgressCallback progressCallback) {
        if (getRules().generateExampleClass()) {
            AbstractJavaGenerator javaGenerator = new ExampleGenerator();
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
// FIXME: 16/7/14 add rules hear
        if (getRules().generateMapperClass()) {
            AbstractJavaGenerator javaGenerator = new EzjsMapperGenerator();
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateRecordWithBLOBsClass()) {
            AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
            initializeAbstractGenerator(javaGenerator, warnings,
                    progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    /**
     * Initialize abstract generator.
     *
     * @param abstractGenerator
     *            the abstract generator
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    protected void initializeAbstractGenerator(
            AbstractGenerator abstractGenerator, List<String> warnings,
            ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#getGeneratedJavaFiles()
     */
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator
                    .getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        context.getJavaModelGeneratorConfiguration()
                                .getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator
                    .getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        context.getJavaClientGeneratorConfiguration()
                                .getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#getGeneratedXmlFiles()
     */
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    true, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#getGenerationSteps()
     */
    @Override
    public int getGenerationSteps() {
        return javaModelGenerators.size() + clientGenerators.size() +
                (xmlMapperGenerator == null ? 0 : 1);
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#isJava5Targeted()
     */
    @Override
    public boolean isJava5Targeted() {
        return true;
    }

    /* (non-Javadoc)
     * @see IntrospectedTable#requiresXMLGenerator()
     */
    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaClientGenerator javaClientGenerator =
                createJavaClientGenerator();

        if (javaClientGenerator == null) {
            return false;
        } else {
            return javaClientGenerator.requiresXMLGenerator();
        }
    }
}
