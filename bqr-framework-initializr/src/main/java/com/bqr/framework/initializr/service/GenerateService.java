package com.bqr.framework.initializr.service;

import com.bqr.framework.initializr.domain.CompileDependency;
import com.bqr.framework.initializr.domain.ProjectRequest;
import com.bqr.framework.initializr.utils.TemplateRenderer;
import com.bqr.framework.initializr.utils.ZipUtils;
import org.apache.tools.ant.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 *
 */
@Service
public class GenerateService
{

    private static final String BQR_FRAMEWORK_STARTER_WEB = "bqr-framework-starter-web";

    private static final String BQR_FRAMEWORK_STARTER_MYBATIS = "bqr-framework-starter-mybatis";

    private static final String BQR_FRAMEWORK_STARTER_REDIS = "bqr-framework-starter-redis";

    private static final String BQR_FRAMEWORK_STARTER_FEIGN = "bqr-framework-starter-feign";

    private static final String BQR_FRAMEWORK_STARTER_SWAGGER = "bqr-framework-starter-swagger";

    private static final String BQR_FRAMEWORK_STARTER_XXLJOB = "bqr-framework-starter-xxljob";

    private static final String BQR_FRAMEWORK_STARTER_CLOUD = "bqr-framework-starter-cloud";

    
    @Autowired
    private TemplateRenderer templateRenderer;
    
    @Value("${TMPDIR:.}/initializr")
    private String tmpdir;

    private File temporaryDirectory;

    private File projectDir;

    private File rootDir;
    
    public File generateProject(ProjectRequest request) throws Exception
    {
        //1. 将参数转换成Map结构
        Map model = convertToMap(request);
        // 2.先生成根目录结构
        rootDir = createRootDir();
        projectDir = new File(rootDir, request.getArtifactId());
        projectDir.mkdir();
        
        // 3.先生成pom文件
        generatePom(model);
        
        // 4.再生成src目录结构
        generateSrc(request, model);

        // 5.生成gitignore文件
        generateGitignore();

        // 6.最后用zip进行压缩
        File zipFile = new File(rootDir,request.getArtifactId() + ".zip");
        ZipUtils.compress(projectDir, zipFile, true);

        return zipFile;
    }

    private Map convertToMap(ProjectRequest request)
    {
        Map model = new HashMap(BeanMap.create(request));
        //设置pom子模块标识
        List<CompileDependency> compileDependencies = request.getCompileDependencies();
        if (!CollectionUtils.isEmpty(compileDependencies))
        {
            compileDependencies.stream().forEach(dependency ->
            {
                if (BQR_FRAMEWORK_STARTER_WEB.equals(dependency.getArtifactId()))
                {
                    model.put("useWebModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_MYBATIS.equals(dependency.getArtifactId()))
                {
                    model.put("useMybatisModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_REDIS.equals(dependency.getArtifactId()))
                {
                    model.put("useRedisModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_FEIGN.equals(dependency.getArtifactId()))
                {
                    model.put("useFeignModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_SWAGGER.equals(dependency.getArtifactId()))
                {
                    model.put("useSwaggerModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_XXLJOB.equals(dependency.getArtifactId()))
                {
                    model.put("useXxlJobModel", true);
                }
                else if (BQR_FRAMEWORK_STARTER_CLOUD.equals(dependency.getArtifactId()))
                {
                    model.put("useCloudModel", true);
                }
            });
        }
        return  model;

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


        //根据加载不同的子模块来创建不同的目录结构
        if (Objects.equals(model.get("useWebModel"), true))
        {
            File controllerDir = new File(mainJavaDir, "controller");
            File serviceDir = new File(mainJavaDir, "service");
            File domainDir = new File(mainJavaDir, "domain");
            controllerDir.mkdir();
            serviceDir.mkdir();
            domainDir.mkdir();
        }

        if (Objects.equals(model.get("useMybatisModel"), true))
        {
            File mapperDir = new File(mainJavaDir, "mapper");
            File entityDir = new File(mainJavaDir, "entity");
            File mapperResourceDir = new File(mainResourceDir, "mapper");
            mapperDir.mkdir();
            entityDir.mkdir();
            mapperResourceDir.mkdir();
        }

        if (Objects.equals(model.get("useFeignModel"), true))
        {
            File feignDir = new File(mainJavaDir, "feign");
            feignDir.mkdir();
        }

        if (Objects.equals(model.get("useXxlJobModel"), true))
        {
            File jobHandlerDir = new File(mainJavaDir, "jobhandler");
            jobHandlerDir.mkdir();
        }


        //创建Application.java启动类
        String javaContent = templateRenderer.process("Application.java", model);
        File applicationJava = new File(mainJavaDir, request.getBootstrapApplicationName() + ".java");
        writeText(applicationJava, javaContent);

        //创建application.yml
        String ymlContent = templateRenderer.process("application.yml", model);
        //如果使用的mybatis模块则将yml配置文件放入resources/config目录下，防止mybatis模块自身的的yml被覆盖;
        File yml = null;
        if (Objects.equals(model.get("useMybatisModel"), true))
        {
            File resourceConfigDir = new File(mainResourceDir, "config");
            resourceConfigDir.mkdir();
            yml = new File(resourceConfigDir, "application.yml");
        }
        else
        {
            yml = new File(mainResourceDir, "application.yml");
        }
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
