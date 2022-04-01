package me.ezjs.generator.plugins;

import me.ezjs.generator.mybatis.api.CommentGenerator;
import me.ezjs.generator.mybatis.api.FullyQualifiedTable;
import me.ezjs.generator.mybatis.api.dom.java.*;
import me.ezjs.generator.mybatis.codegen.AbstractJavaGenerator;
import me.ezjs.generator.mybatis.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero-mac on 16/7/15.
 */
public class EzjsMapperGenerator extends AbstractJavaGenerator {

    private static final String GENERIC_MAPPER = "me.ezjs.core.mapper.GenericMapper";
    private static final String GENERIC_MANAGER = "me.ezjs.core.manager.GenericManager";
    private static final String GENERIC_MANAGER_IMPL = "me.ezjs.core.manager.impl.GenericManagerImpl";

    public EzjsMapperGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(Messages.getString(
                "Progress.19", table.toString())); //$NON-NLS-1$
        CommentGenerator commentGenerator = context.getCommentGenerator();

        String basePackageType = introspectedTable.getBasePackageType();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getEzjsMapperType());
        String modelName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        String modelClassType = basePackageType + ".model." + modelName;

        Interface mapperInterface = new Interface(type);
        mapperInterface.setVisibility(JavaVisibility.PUBLIC);

        // mapper interface
        mapperInterface.addAnnotation("@Mapper");
        mapperInterface.addSuperInterface(new FullyQualifiedJavaType("GenericMapper<" + modelName + ", Integer>"));

        mapperInterface.addImportedType(new FullyQualifiedJavaType(GENERIC_MAPPER));
        mapperInterface.addImportedType(new FullyQualifiedJavaType(modelClassType));
        mapperInterface.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));

        // sql provider
        TopLevelClass sqlProviderClass = new TopLevelClass(new FullyQualifiedJavaType(basePackageType + ".mapper.sqlprovider." + modelName + "Provider"));
        sqlProviderClass.setVisibility(JavaVisibility.PUBLIC);
        sqlProviderClass.setSuperClass(new FullyQualifiedJavaType("SqlProvider"));

        sqlProviderClass.addImportedType(new FullyQualifiedJavaType("me.ezjs.core.mapper.SqlProvider"));
        sqlProviderClass.addImportedType(new FullyQualifiedJavaType("org.apache.commons.logging.Log"));
        sqlProviderClass.addImportedType(new FullyQualifiedJavaType("org.apache.commons.logging.LogFactory"));

        // add log field in sql provider.
        Field logField = new Field();
        logField.setVisibility(JavaVisibility.PROTECTED);
        logField.setType(new FullyQualifiedJavaType("Log"));
        logField.setName("log"); //$NON-NLS-1$
        logField.setFinal(true);
        logField.setInitializationString("LogFactory.getLog(getClass())");
        sqlProviderClass.addField(logField);

        // add manager interface
        String managerType = basePackageType + ".manager.";
        Interface managerInterface = new Interface(managerType + modelName + "Manager");
        managerInterface.setVisibility(JavaVisibility.PUBLIC);

        managerInterface.addSuperInterface(new FullyQualifiedJavaType("GenericManager<" + modelName + ", Integer>"));

        managerInterface.addImportedType(new FullyQualifiedJavaType(modelClassType));
        managerInterface.addImportedType(new FullyQualifiedJavaType(GENERIC_MANAGER));

        // add manager class
        TopLevelClass managerClass = new TopLevelClass(managerType + "impl." + modelName + "ManagerImpl");
        managerClass.setVisibility(JavaVisibility.PUBLIC);

        managerClass.addAnnotation("@Service");

        managerClass.addSuperInterface(new FullyQualifiedJavaType(modelName + "Manager"));
        managerClass.setSuperClass(new FullyQualifiedJavaType("GenericManagerImpl<" + modelName + ", Integer>"));

        Method managerConstructor = new Method();
        managerConstructor.setVisibility(JavaVisibility.PUBLIC);
        managerConstructor.setConstructor(true);
        managerConstructor.setName(modelName + "ManagerImpl");
        managerConstructor.addAnnotation("@Autowired");
        managerConstructor.addParameter(0, new Parameter(new FullyQualifiedJavaType(modelName + "Mapper"), "mapper"));
        managerConstructor.addBodyLine("super(mapper, " + modelName + ".class);"); //$NON-NLS-1$

        managerClass.addImportedType(GENERIC_MANAGER_IMPL);
        managerClass.addImportedType(getImport(basePackageType, modelName, "model"));
        managerClass.addImportedType(getImport(basePackageType, modelName, "manager"));
        managerClass.addImportedType(getImport(basePackageType, modelName, "mapper"));
        managerClass.addImportedType(modelClassType);
        managerClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        managerClass.addImportedType("org.springframework.stereotype.Service");
        managerClass.addMethod(managerConstructor);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(mapperInterface);
        answer.add(sqlProviderClass);
        answer.add(managerInterface);
        answer.add(managerClass);
        return answer;
    }

    private String getImport(String basePackage, String modelName, String type) {
        if (type.equals("model")) {
            return basePackage + ".model." + modelName;
        } else if (type.equals("mapper")) {
            return basePackage + ".mapper." + modelName + "Mapper";
        } else if (type.equals("sqlProvider")) {
            return basePackage + ".mapper.sqlprovider." + modelName + "SqlProvider";
        } else if (type.equals("manager")) {
            return basePackage + ".manager." + modelName + "Manager";
        } else if (type.equals("managerImpl")) {
            return basePackage + ".manager.impl." + modelName + "ManagerImpl";
        }
        return null;
    }

}
