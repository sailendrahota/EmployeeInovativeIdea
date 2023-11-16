package com.fiserve.zelle;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
//@EnableEurekaClient
@Configuration
public class ZelleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZelleApplication.class, args);
	}
	
	@Bean
	//@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest()
	      .permitAll())
	      .csrf(AbstractHttpConfigurer::disable);
	    http.headers().frameOptions().disable();
	    return http.build();
	    
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	    		  
	         .apis(RequestHandlerSelectors.basePackage("com.fiserve.zelle")).build().apiInfo(apiEndPointsInfo())
	         .securitySchemes(Lists.newArrayList(apiKey()));
	   }
	
	private ApiKey apiKey() {    
	    return new ApiKey("Authorization", "Authorization", "header"); 
	}
	
	public static final Contact DEFAULT_CONTACT = new Contact(
		      "ImmagineInovet", "http://expleogroup.com/", "testmail@imagineInovet.com");
	
	private ApiInfo apiEndPointsInfo() {
	        return new ApiInfoBuilder()
	        	
	        	.title("Employee")
	        	.description("RESTfull service for Employee application. For this sample, you can set the api key  (Bearer <your JWT token>)  to test the authorized service."  )
	           
	        	.version("1.0.0")
	        	 .contact(DEFAULT_CONTACT)
	            .build();
	    }
}


