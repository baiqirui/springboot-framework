package {{packageName}};



import org.springframework.boot.SpringApplication;
{{^useCloudModel}}
import org.springframework.boot.autoconfigure.SpringBootApplication;
{{/useCloudModel}}
{{#useCloudModel}}
import org.springframework.cloud.client.SpringCloudApplication;
{{/useCloudModel}}
{{#useFeignModel}}
import org.springframework.cloud.netflix.feign.EnableFeignClients;
{{/useFeignModel}}
{{#useMybatisModel}}
import org.springframework.context.annotation.ComponentScan;
{{/useMybatisModel}}




{{#useCloudModel}}
@SpringCloudApplication
{{/useCloudModel}}
{{#useFeignModel}}
@EnableFeignClients
{{/useFeignModel}}
{{^useCloudModel}}
@SpringBootApplication
{{/useCloudModel}}
{{#useMybatisModel}}
@ComponentScan({"com.bqr", "{{packageName}}"})
{{/useMybatisModel}}
public class {{bootstrapApplicationName}}
{
	public static void main(String[] args)
	{
		SpringApplication.run({{bootstrapApplicationName}}.class, args);
	}
}
