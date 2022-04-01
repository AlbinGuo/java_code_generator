package me.ezjs.generator;

import me.ezjs.generator.mybatis.config.Context;
import me.ezjs.generator.mybatis.config.JDBCConnectionConfiguration;
import org.junit.Test;
import me.ezjs.generator.mybatis.api.MyBatisGenerator;
import me.ezjs.generator.mybatis.config.Configuration;
import me.ezjs.generator.mybatis.config.xml.ConfigurationParser;
import me.ezjs.generator.mybatis.exception.InvalidConfigurationException;
import me.ezjs.generator.mybatis.exception.XMLParserException;
import me.ezjs.generator.mybatis.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero-mac on 16/7/14.
 */
public class MybatisGeneratorTest {

    @Test
    public void generate() throws InterruptedException, SQLException, IOException, XMLParserException, InvalidConfigurationException {

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
        InputStream stream = ClassLoader.getSystemResourceAsStream("generatorConfig.xml");

        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(stream);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null, null, null, true);
    }

}
