package com.bqr.framework.initializr.service;

import com.bqr.framework.initializr.domain.ProjectRequest;
import com.bqr.framework.initializr.utils.TemplateRenderer;
import com.bqr.framework.initializr.utils.ZipUtils;
import org.apache.tools.ant.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/**
 *
 */
@Service
public class GenerateService
{
    
    @Autowired
    private TemplateRenderer templateRenderer;
    
    @Value("${TMPDIR:.}/initializr")
    private String tmpdir;

    private File temporaryDirectory;

    private File projectDir;

    private File rootDir;
    
    public File generateProject(ProjectRequest request) throws Exception
    {
        Map model = BeanMap.create(request);
        // 1.先生成根目录结构
        rootDir = createRootDir();
        projectDir = new File(rootDir, request.getArtifactId());
        projectDir.mkdir();
        
        // 2.先生成pom文件
        generatePom(model);
        
        // 4.再生成src目录结构
        generateSrc(request, model);

        // 5.生成gitignore文件
        generateGitignore();

        // 6.最后用zip进行压缩
        File zipFile = new File(rootDir,request.getArtifactId() + ".zip");
        ZipUtils.compress(projectDir, zipFile);

        return zipFile;
    }

    private void generateGitignore()
    {
        String body = templateRenderer.process("gitignore.tmpl", null);
        writeText(new File(projectDir, ".gitignore"), body);
    }


    /**
     * 创建Src源码目录
     *
     * @param request
     * @param model
     */
    private void generateSrc(ProjectRequest request, Map model)
    {
        //先生成main目录和test目录
        StringBuffer mainJavaDirPath = new StringBuffer("src");
        mainJavaDirPath.append(File.separator).append("main").append(File.separator).append("java").append(File.separator);

        StringBuffer mainResourceDirPath = new StringBuffer("src");
        mainResourceDirPath.append(File.separator).append("main").append(File.separator).append("resources").append(File.separator);

        StringBuffer testDirPath = new StringBuffer("src");
        testDirPath.append(File.separator).append("test").append(File.separator).append("java").append(File.separator);

        String groupId = request.getGroupId();
        if (null != groupId && groupId.trim().length() > 0)
        {
           String[] packageDir = groupId.split("\\.");
           for (int i = 0; i < packageDir.length; i++)
           {
               mainJavaDirPath.append(packageDir[i]).append(File.separator);
               testDirPath.append(packageDir[i]).append(File.separator);
           }
        }
        mainJavaDirPath.append(request.getArtifactId());
        testDirPath.append(request.getArtifactId());

        File mainJavaDir = new File(projectDir, mainJavaDirPath.toString());
        File testDir = new File(projectDir, testDirPath.toString());
        File mainResourceDir = new File(projectDir, mainResourceDirPath.toString());

        mainJavaDir.mkdirs();
        testDir.mkdirs();
        mainResourceDir.mkdirs();

        //创建Application.java启动类
        String javaContent = templateRenderer.process("Application.java", model);
        File applicationJava = new File(mainJavaDir, request.getBootstrapApplicationName() + ".java");
        writeText(applicationJava, javaContent);

        //创建application.yml
        String ymlContent = templateRenderer.process("application.yml", model);
        File yml = new File(mainResourceDir, "application.yml");
        writeText(yml, ymlContent);

    }

    private File createRootDir()
    {
        try
        {
            File rootDir = File.createTempFile("tmp", "", getTemporaryDirectory());
            rootDir.delete();
            rootDir.mkdirs();
            return rootDir;
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Cannot create temp dir", e);
        }
    }
    
    private File getTemporaryDirectory()
    {
        if (temporaryDirectory == null)
        {
            temporaryDirectory = new File(tmpdir);
            temporaryDirectory.mkdirs();
        }
        return temporaryDirectory;
    }
    
    private void generatePom(Map model)
    {
        String pomContent = templateRenderer.process("pom-template.xml", model);
        File pom = new File(projectDir, "pom.xml");
        writeText(pom, pomContent);
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

    public void cleanProjectDir()
    {
        FileSystemUtils.deleteRecursively(rootDir);
    }
}
