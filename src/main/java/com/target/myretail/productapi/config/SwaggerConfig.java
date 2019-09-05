package com.target.myretail.productapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("MyRetail Product API")
                .description("Returns Product Information of MyRetail products")
                .version("1.0.0")
                .contact(new Contact("Dhanasekaran Rajendran", "", "dhanasekaran.rajendran@gmail.com"))
                .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.target.myretail.productapi.comtroller"))
                .build().apiInfo(apiInfo());
    }
}
