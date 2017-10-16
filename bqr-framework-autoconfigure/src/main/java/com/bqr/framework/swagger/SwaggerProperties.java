package com.bqr.framework.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置属性
 *
 * @author
 * @Date 2017-10-12 16:59
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties
{
    private String groupName;
    
    private String basePackage;
    
    private String title;
    
    private String version;
    
    private String description;
    
}
