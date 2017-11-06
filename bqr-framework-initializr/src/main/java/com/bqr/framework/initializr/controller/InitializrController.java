package com.bqr.framework.initializr.controller;

import com.bqr.framework.initializr.domain.CompileDependency;
import com.bqr.framework.initializr.domain.ProjectRequest;
import com.bqr.framework.initializr.service.GenerateService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class InitializrController
{

    @Autowired
    private GenerateService generateService;

    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap)
        throws Exception
    {
        ObjectMapper om = new ObjectMapper();
        List<CompileDependency> dependencies = om.readValue(
            ResourceUtils.getURL("classpath:static/json/dependencies.json"),
            new TypeReference<List<CompileDependency>>()
            {
            });
        modelMap.addAttribute("dependencies", dependencies);
        modelMap.addAttribute("frameworkVersion", "0.1.0.RELEASE");

        return "index";
    }


    @RequestMapping(value = "/generate")
    public String generate(ProjectRequest request)
            throws IOException
    {
        System.out.println("generate............" + request);
        File projectFile = generateService.generateProject(request);
        return "index";
    }
    
}
