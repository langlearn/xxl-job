package com.union.express.xxl.job;

import com.xxl.job.core.executor.jetty.XxlJobExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.glue.loader.impl.DbGlueLoader;
import com.xxl.job.core.registry.impl.DbRegistHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by yjj on 2017/3/1.
 */
@Configuration
public class XxxJobConfiguration {
    @Bean(name = "dataSource",destroyMethod="close")
    @Qualifier(value = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "c3p0")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(com.mchange.v2.c3p0.ComboPooledDataSource.class).build();
    }

    @Bean
    public DbGlueLoader dbGlueLoader(){
        DbGlueLoader dbGlueLoader = new DbGlueLoader();
        dbGlueLoader.setDataSource(dataSource());
        return dbGlueLoader;
    }

    @Bean
    public GlueFactory glueFactory(){
        GlueFactory glueFactory = new GlueFactory();
        glueFactory.setCacheTimeout(10000);
        glueFactory.setGlueLoader(dbGlueLoader());
        return glueFactory;
    }

    @Bean
    public DbRegistHelper dbRegistHelper(){
        DbRegistHelper dbRegistHelper = new DbRegistHelper();
        dbRegistHelper.setDataSource(dataSource());
        return dbRegistHelper;
    }

    @Bean(destroyMethod = "destroy",initMethod = "start")
    public XxlJobExecutor xxlJobExecutor(){
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setPort(9999);
        xxlJobExecutor.setAppName("xxl-job-executor-example");
        xxlJobExecutor.setRegistHelper(dbRegistHelper());
        return xxlJobExecutor;
    }
}
