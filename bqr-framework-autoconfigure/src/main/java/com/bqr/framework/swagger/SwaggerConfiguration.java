package com.bqr.framework.swagger;//package com.bqr.framework.swagger;



import com.bqr.framework.ConditionalOnMapProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.DependsOn;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ConditionalOnClass(Docket.class)
@ConditionalOnMapProperty(prefix = "swagger.")
@ConditionalOnProperty(value = "swagger.enable", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration
{

    @Bean
    @DependsOn(value = "swaggerApiInfo")
    public Docket createDocket(SwaggerProperties swaggerProperties, ApiInfo apiInfo)
    {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerProperties.getGroupName())
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true).enableUrlTemplating(false)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .build()
                .apiInfo(apiInfo);


        return docket;
    }

    @Bean("swaggerApiInfo")
    public ApiInfo createApi(SwaggerProperties swaggerProperties)
    {


            return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle()) // 标题
                .description(swaggerProperties.getDescription()) // 描述
                .termsOfServiceUrl("") //网址
                .version(swaggerProperties.getVersion()) // 版本号
                .build();
    }

}