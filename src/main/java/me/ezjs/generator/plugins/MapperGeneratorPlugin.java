package me.ezjs.generator.plugins;

import me.ezjs.generator.mybatis.api.GeneratedJavaFile;
import me.ezjs.generator.mybatis.api.IntrospectedTable;
import me.ezjs.generator.mybatis.api.PluginAdapter;
import me.ezjs.generator.mybatis.api.dom.java.CompilationUnit;
import me.ezjs.generator.mybatis.codegen.AbstractJavaGenerator;
import me.ezjs.generator.mybatis.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero-mac on 16/7/14.
 */
public class MapperGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable) {
        return null;
    }
}
