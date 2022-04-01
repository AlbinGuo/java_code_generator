package me.ezjs.generator;

import me.ezjs.core.exception.AppException;
import me.ezjs.generator.mybatis.api.MyBatisGenerator;
import me.ezjs.generator.mybatis.config.*;
import me.ezjs.generator.mybatis.config.xml.ConfigurationParser;
import me.ezjs.generator.mybatis.exception.InvalidConfigurationException;
import me.ezjs.generator.mybatis.exception.XMLParserException;
import me.ezjs.generator.mybatis.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zero-mac on 17/8/11.
 */
public class CoreGenerator {

    private final static String DEFAULT_CONTEXT = "default";

    private static final String MysqlHost = "mysql.host";
    private static final String MysqlPort = "mysql.port";
    private static final String MysqlDB = "mysql.db";
    private static final String MysqlUser = "mysql.user";
    private static final String MysqlPass = "mysql.pass";

    private static final String PKG_PREFIX = "me.ezjs.";//默认包的前缀
    private static final String targetModule = "target.module";
    private static final String targetDir = "target.dir";//目标目录,默认设置为当前执行的目录,即jar包(执行文件)的目录

    private static final String TABLES_KEY = "tables";

    private static Map<String, List<String>> cmdParams;

    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {

        parseCmdArgs(args);

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        //指定 逆向工程配置文件
        InputStream stream = ClassLoader.getSystemResourceAsStream("generatorConfig.xml");

        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(stream);

        Context defaultContext = config.getContext(DEFAULT_CONTEXT);

        //mysql
        JDBCConnectionConfiguration jdbcConnectionConfiguration = defaultContext.getJdbcConnectionConfiguration();
        String mysqlHost = getParam(MysqlHost, "localhost");
        String mysqlPort = getParam(MysqlPort,"3306");
        String mysqlDb = getParam(MysqlDB);
        String mysqlURL = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDb + "?useUnicode=true&useSSL=false";
        jdbcConnectionConfiguration.setConnectionURL(mysqlURL);
        jdbcConnectionConfiguration.setUserId(getParam(MysqlUser));
        jdbcConnectionConfiguration.setPassword(getParam(MysqlPass));
//defaultContext.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        //target
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = defaultContext.getJavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage(PKG_PREFIX + getParam(targetModule));
        javaModelGeneratorConfiguration.setTargetProject(getParam(targetDir));
//defaultContext.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);


        List<String> tables = getParamList(TABLES_KEY);

        List<TableConfiguration> tcs = defaultContext.getTableConfigurations();
        while (tcs.size() > 0) {
            tcs.remove(0);
        }

        tables.forEach(table -> {
            TableConfiguration tc = new TableConfiguration(new Context(null));
            tc.setTableName(table);
            tcs.add(tc);
        });

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);

        myBatisGenerator.generate(null, null, null, true);
    }

    private static void parseCmdArgs(String[] args) {
        cmdParams = new HashMap<>();

        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                cmdParams.put(a.substring(1), options);
            } else if (options != null) {
                options.add(a);
            } else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }
    }

    public static String getParam(String key) {
        List<String> strings = cmdParams.get(key);
        if (strings == null || strings.size() == 0) {
            throw new AppException("缺少必要参数: " + key);
        }
        return cmdParams.get(key).get(0);
    }

    public static String getParam(String key, String defaultVal) {
        List<String> strings = cmdParams.get(key);
        if (strings == null || strings.size() == 0) {
            return defaultVal;
        }
        return strings.get(0);
    }

    public static List<String> getParamList(String key) {
        return cmdParams.get(key);
    }
}
