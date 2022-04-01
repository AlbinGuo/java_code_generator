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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import me.ezjs.generator.mybatis.config.Configuration;
import me.ezjs.generator.mybatis.config.ConnectionFactoryConfiguration;
import me.ezjs.generator.mybatis.config.Context;
import me.ezjs.generator.mybatis.config.JDBCConnectionConfiguration;
import me.ezjs.generator.mybatis.internal.DefaultShellCallback;
import org.junit.Test;
import me.ezjs.generator.mybatis.api.MyBatisGenerator;
import me.ezjs.generator.mybatis.config.ModelType;
import me.ezjs.generator.mybatis.config.xml.ConfigurationParser;
import me.ezjs.generator.mybatis.exception.InvalidConfigurationException;

public class MyBatisGeneratorTest {

    @Test(expected = InvalidConfigurationException.class)
    public void testGenerateMyBatis3WithInvalidConfig() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigMyBatis3_badConfig.xml"));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(2, e.getErrors().size());
            throw e;
        }
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testGenerateIbatis2WithInvalidConfig() throws Exception {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("generatorConfigIbatis2_badConfig.xml"));

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(1, e.getErrors().size());
            throw e;
        }
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testGenerateInvalidConfigWithNoConnectionSources() throws Exception {
        List<String> warnings = new ArrayList<String>();
        Configuration config = new Configuration();
        Context context = new Context(ModelType.HIERARCHICAL);
        context.setId("MyContext");
        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(3, e.getErrors().size());
            throw e;
        }
    }

    @Test(expected = InvalidConfigurationException.class)
    public void testGenerateInvalidConfigWithTwoConnectionSources() throws Exception {
        List<String> warnings = new ArrayList<String>();
        Configuration config = new Configuration();
        Context context = new Context(ModelType.HIERARCHICAL);
        context.setId("MyContext");
        context.setConnectionFactoryConfiguration(new ConnectionFactoryConfiguration());
        context.setJdbcConnectionConfiguration(new JDBCConnectionConfiguration());
        config.addContext(context);

        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        try {
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(null, null, null, false);
        } catch (InvalidConfigurationException e) {
            assertEquals(3, e.getErrors().size());
            throw e;
        }
    }
}
