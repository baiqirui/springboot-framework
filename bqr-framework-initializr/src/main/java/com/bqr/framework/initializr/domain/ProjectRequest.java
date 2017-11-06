package com.bqr.framework.initializr.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Likai on 2017/10/13 0013.
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProjectRequest
{
    private String bootstrapApplicationName = "DemoApplication";
    
    private String packageName = "com.example.demo";
    
    private String language = "java";
    
    @NotNull(message = "groupId 不能为空!")
    private String groupId;
    
    @NotNull(message = "artifactId 不能为空!")
    private String artifactId;
    
    private String version = "1.0.0-SNAPSHOT";
    
    private List<CompileDependency> compileDependencies;
    
    private String frameworkVersion;
    
    private String mavenPluginVersion = "1.5.8.RELEASE";
}
