package com.bqr.framework.initializr.service;

import com.bqr.framework.initializr.domain.ProjectRequest;
import com.bqr.framework.initializr.utils.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 */
@Service
public class GenerateService
{
    
    @Autowired
    private TemplateRenderer templateRenderer;
    
    public File generateProject(ProjectRequest request)
    {
        Map model = BeanMap.create(request);
        // 1.先生成pom文件
        File pom = generatePom(model);
        
        // 2.再生成项目目录结构
        
        // 3.最后用zip进行压缩
        
        return null;
    }
    
    private File generatePom(Map model)
    {
        String pomContent = templateRenderer.process("pom-template.xml", model);
        File pom = new File("G:\\code\\pom.xml");
        writeText(pom, pomContent);
        return pom;
    }


    private void writeText(File target, String body)
    {
        try (OutputStream stream = new FileOutputStream(target))
        {
            StreamUtils.copy(body, Charset.forName("UTF-8"), stream);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Cannot write file " + target, e);
        }
    }
}
