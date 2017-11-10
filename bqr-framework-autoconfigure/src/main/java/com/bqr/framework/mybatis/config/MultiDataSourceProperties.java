package com.bqr.framework.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "datasource")
public class MultiDataSourceProperties
{
    private DataSourceProperties writeDS;
    
    private DataSourceProperties readDS;
}
