package com.portfolio.management.app.config.swagger;

import static springfox.documentation.builders.RequestHandlerSelectors.withClassAnnotation;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	// See: https://github.com/noirbizarre/flask-restplus/issues/583#issuecomment-469036725
	private static final String HIDE_MODELS_CSS = "<style>.models {display: none !important}</style>";

	@Bean
	public Docket workforceManagementApi() {

		return new Docket(SWAGGER_2).select()
				.apis(withClassAnnotation(RestController.class))
				.build()
				//.tags(new Tag("v2", "Portfolio API for version v2"))
				.apiInfo(apiInfo());
	}

    private ApiInfo apiInfo() {
    	return new ApiInfoBuilder()
		.title("Investment Portfolio Simulator REST API")
		.description("Investment Portfolio Application for simulating investment activity." + HIDE_MODELS_CSS)
		.version("1.0")
		.termsOfServiceUrl("Terms of service URL")
		.contact(new Contact("Contact name", "Contact URL", "Contact email"))
		.license("License of API")
		.licenseUrl("License URL")
		.build();
    }
}
