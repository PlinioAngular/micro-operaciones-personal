package com.proyecto.everis;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EnableJpaRepositories("com.proyecto.everis.*")
@ComponentScan({ "com.proyecto.everis.resource","com.proyecto.everis.service.impl","com.proyecto.everis.service"})
@EnableReactiveMongoRepositories(basePackages = { "com.proyecto.everis.repository","com.proyecto.everis.service","com.proyecto.everis.service.impl"})
public class MicroOperacionesApplication extends AbstractReactiveMongoConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(MicroOperacionesApplication.class, args);
	}
	
	@Value("${app.db}")
	private String db;
	
	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create();
	}

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return db;
	}
	
	public static final Contact DEFAULT_CONTACT = new Contact("Everis Bootcamp", "https://www.everis.com",
			"dfernava@everis.com");
	
	public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Documentation MICRO-OPERACIONES-PERSONAL API", "Documentation MICRO-OPERACIONES-PERSONAL API", "1.0",
			"PREMIUM", DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
			new ArrayList<>());
    
	@Bean
	public Docket swaggerPersonApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.proyecto.everis.resource"))
					.paths(PathSelectors.any())
				.build()
				.apiInfo(DEFAULT_API_INFO);
	}

}
