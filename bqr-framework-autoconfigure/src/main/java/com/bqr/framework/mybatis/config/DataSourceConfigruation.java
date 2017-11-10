package com.bqr.framework.mybatis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.bqr.framework.ConditionalOnMapProperty;

@Configuration
@ConditionalOnMapProperty(prefix = "datasource.")
@EnableConfigurationProperties(MultiDataSourceProperties.class)
public class DataSourceConfigruation
{

    @Bean(name = "dynamicDataSource")
    public DataSource createDataSource(MultiDataSourceProperties properties)
    {
        //写数据源是必填项;
        if (null == properties || null == properties.getWriteDS())
        {
            throw new IllegalArgumentException("Property 'writeDS' is required");
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        //创建写数据，并设置为默认数据源
        DataSource writeDataSource = createDruidDataSource(properties.getWriteDS());
        dynamicDataSource.setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.WRITE.name(), writeDataSource);
        //如果设置了读数据源，则再创建读数据源;
        if (null != properties.getReadDS())
        {
            DataSource readDataSource = createDruidDataSource(properties.getReadDS());
            targetDataSources.put(DataSourceType.READ.name(), readDataSource);
        }

        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;

    }

    /**
     * 默认创建DruidDataSource
     *
     * @param properties
     * @return
     */
    private DataSource createDruidDataSource(DataSourceProperties properties)
    {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(properties.getUrl());
        datasource.setDriverClassName(properties.getDriverClassName());
        datasource.setUsername(properties.getUsername());
        datasource.setPassword(properties.getPassword());
        datasource.setInitialSize(properties.getInitialSize());
        datasource.setMinIdle(properties.getMinIdle());
        datasource.setMaxWait(properties.getMaxWait());
        datasource.setMaxActive(properties.getMaxActive());
        datasource.setValidationQuery(properties.getValidationQuery());
        datasource.setTimeBetweenConnectErrorMillis(properties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        datasource.setTestWhileIdle(properties.isTestWhileIdle());
        datasource.setTestOnBorrow(properties.isTestOnBorrow());
        datasource.setTestOnReturn(properties.isTestOnReturn());

        return datasource;
    }


}
