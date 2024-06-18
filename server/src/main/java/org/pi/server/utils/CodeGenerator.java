package org.pi.server.utils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.util.Collections;

/**
 * mybatis-plus代码生成器
 * @see <a href="https://baomidou.com/guide/generator.html">generator</a>
 * @author hu1hu
 */

public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://120.77.76.40:3306/pim", "root", "ZAQ1@wsx")
                .globalConfig(builder -> builder // 全局配置
                        .disableOpenDir() // 禁止打开输出目录
                        .author("hu1hu") // 设置作者
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java") // 指定输出目录
                        .commentDate("yyyy-MM-dd") // 注释日期
                )

                .packageConfig(builder -> builder  // 包配置 位置
                        .parent("org.pi.server")  // 设置父包名
                        .entity("model.entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .controller("controller")
                        // 将Mapper xml生成到resources目录下
                        .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")+"/src/main/resources/mapper"))
                )
                .strategyConfig(builder -> builder // 策略配置
                        .enableSkipView()
                        .addTablePrefix("t_") // 设置过滤表前缀
                        .addFieldSuffix("") // 设置过滤字段后缀
                        .addFieldPrefix("") // 设置过滤字段前缀
                        .addFieldSuffix("") // 设置过滤字段后缀

                        // 实体配置策略
                        .entityBuilder()
                        .disableSerialVersionUID() // 禁用生成 serialVersionUID
                        .enableFileOverride() // 开启覆盖已生成文件
                        .enableLombok()
                        .javaTemplate("templates/entity.java")
                        .naming(NamingStrategy.underline_to_camel) // 实体命名策略 -- 下划线转驼峰命名
                        .columnNaming(NamingStrategy.underline_to_camel) // 列命名策略 -- 下划线转驼峰命名

                        // Mapper 配置策略
                        .mapperBuilder()
                        .enableFileOverride() // 开启覆盖已生成文件
                        .superClass(BaseMapper.class) // 设置父类
                        .formatMapperFileName("%sMapper") // 设置 mapper 类名
                        .formatXmlFileName("%sMapper") // 设置 xml 类名
                        .enableBaseResultMap() // 开启 BaseResultMap

                        // controller 配置策略
                        // 禁用生成 Controller
                        .controllerBuilder()
                        .disable() // 禁用生成 Controller
                        .formatFileName("") // 设置 controller 类名

                        // Service 配置策略
                        // 禁用生成 Service
                        .serviceBuilder()
                        .disable() // 禁用生成 Service
                        .formatServiceImplFileName("") // 设置 service 实现类名

                )
                /*
                模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker 或 Enjoy
               .templateEngine(new BeetlTemplateEngine())
               .templateEngine(new FreemarkerTemplateEngine())
               .templateEngine(new EnjoyTemplateEngine())
                */
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
