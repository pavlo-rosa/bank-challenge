package com.prosa.rivertech.rest.bankservices.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Configuration
//Enable Swagger
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Set<String> DEFAULT_PRODUCERS_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json"));
    public static final String TAG_ACCOUNT = "Accounts Controller";
    public static final String TAG_TRANSACTION = "Transactions Controller";
    public static final String TAG_USER = "Users Controller";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Bank Service API")
                .description("This is a project test to evaluate Pavlo Rosa's the programming skills")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(new Contact("Pavlo", "https://pavlorosa.com/", "pavlo.rosa@gmail.com"))
                .version("1.0.0")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(DEFAULT_PRODUCERS_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCERS_AND_CONSUMES)
                .tags(new Tag(TAG_ACCOUNT, "Operations related clients accounts"))
                .tags(new Tag(TAG_USER, "CRUD of Bank's clients"))
                .tags(new Tag(TAG_TRANSACTION, "Operations to retrieve transactions and generate operations"))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();

    }
}
