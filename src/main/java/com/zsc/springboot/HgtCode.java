package com.zsc.springboot;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

//生成器
public class HgtCode {
    public static void main(String[] args) {
        //需要构建一个代码生成器 对象
        AutoGenerator mpg = new AutoGenerator();

        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("HGT");
        gc.setOpen(false);
        gc.setFileOverride(false);  // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setIdType(IdType.ID_WORKER);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);

        mpg.setGlobalConfig(gc);

        // 2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/campus_helper?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);

        mpg.setDataSource(dsc);

        // 3、包的配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("springboot");  // 模块
        pc.setParent("com.zsc");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");

        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("user"); // 可变长参数
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass(); // 父类实体，没有,不用设置
        strategy.setEntityLombokModel(true);
        //strategy.setRestControllerStyle(true);

        strategy.setLogicDeleteFieldName("deleted");

        // 自动填充配置
        //TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        //TableFill gmtModified = new TableFill("gmt_modified", FieldFill.UPDATE);
        TableFill gmtCreate = new TableFill("create_time", FieldFill.INSERT);
        //TableFill gmtModified = new TableFill("update_time", FieldFill.UPDATE);
        TableFill gmtModified = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(gmtCreate);
        tableFills.add(gmtModified);
        strategy.setTableFillList(tableFills);

        //乐观锁
        strategy.setVersionFieldName("version");

        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);

        mpg.setStrategy(strategy);

        mpg.execute();  // 执行
    }
}