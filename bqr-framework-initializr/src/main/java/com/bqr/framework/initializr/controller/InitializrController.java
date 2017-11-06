package com.bqr.framework.initializr.controller;

import com.bqr.framework.initializr.domain.CompileDependency;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class InitializrController
{
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap)
        throws IOException
    {
        ObjectMapper om = new ObjectMapper();
        List<CompileDependency> dependencies = om.readValue(
            ResourceUtils.getURL("classpath:static/json/dependencies.json"),
            new TypeReference<List<CompileDependency>>()
            {
            });
        modelMap.put("dependencies", dependencies);
        modelMap.put("version", "0.1.0.RELEASE");
        return "index";
    }
    
}
