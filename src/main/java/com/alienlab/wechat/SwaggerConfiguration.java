package com.alienlab.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Wang on 2017/3/29.
 */
@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/wechatcore-api/.*";

    @Value("${alienlab.swagger.contactName}")
    String contactName;
    @Value("${alienlab.swagger.contactUrl}")
    String contactUrl;
    @Value("${alienlab.swagger.contactEmail}")
    String contactEmail;
    @Value("${alienlab.swagger.title}")
    String title;
    @Value("${alienlab.swagger.description}")
    String description;
    @Value("${alienlab.swagger.version}")
    String version;
    @Value("${alienlab.swagger.termsOfServiceUrl}")
    String termsOfServiceUrl;
    @Value("${alienlab.swagger.license}")
    String license;
    @Value("${alienlab.swagger.licenseUrl}")
    String licenseUrl;

    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
                contactName,
                contactUrl,
                contactEmail);

        ApiInfo apiInfo = new ApiInfo(
                title,
                description,
                version,
                termsOfServiceUrl,
                contact,
                license,
                licenseUrl);

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
