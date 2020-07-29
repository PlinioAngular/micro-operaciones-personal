package com.proyecto.everis.util;

import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.model.Bank;
import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Product;
import com.proyecto.everis.service.IAccountStateService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Clase de metodos para ayudar en la validacion y registros
public class MethodsAccount {
	
	@Autowired
	private IAccountStateService servicestate;
	
	//Lista todo los clientes
	public Flux<Client> listAllClient() {
		WebClient webClient=WebClient.create("http://localhost:8080");
		Flux<Client> client=webClient.get().uri("/clients")
				.retrieve().bodyToFlux(Client.class);
		
		return client;
	}
	
	//Lista un cliente por id
	public Mono<Client> listOneClient(@PathVariable String id) {
		Mono<Client> client;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/clients/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			client=webClient.get().uri("/clients/"+id)
					.retrieve().bodyToMono(Client.class);
		}else {
			client=null;
		}	
		
		return client;
	}
	
	//Lista todo los productos
	public Flux<Product> listAllProduct() {
		WebClient webClient=WebClient.create("http://localhost:8080");
		Flux<Product> product=webClient.get().uri("/products")
				.retrieve().bodyToFlux(Product.class);
		
		return product;
	}
	
	//Lista un producto por id
	public Mono<Product> listOneProduct(@PathVariable String id) {
		Mono<Product> product;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/products/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			product=webClient.get().uri("/products/"+id)
					.retrieve().bodyToMono(Product.class);
		}else {
			product=null;
		}	
		
		return product;
	}
	
	//Lista un banco por id
	public Mono<Bank> listOneBank(@PathVariable String id) {
		Mono<Bank> bank;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/banks/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			bank=webClient.get().uri("/banks/"+id)
					.retrieve().bodyToMono(Bank.class);
		}else {
			bank=null;
		}	
		
		return bank;
	}
	
	//Lista todo los bancos
	public Flux<Bank> listAllBank() {
		WebClient webClient=WebClient.create("http://localhost:8080");
		Flux<Bank> bank=webClient.get().uri("/banks")
				.retrieve().bodyToFlux(Bank.class);
		
		return bank;
	}
	
	//Retorna fecha del primer dia del mes actual
	public LocalDateTime firstDay() {
		LocalDateTime hoy=LocalDateTime.now();
		int anio=hoy.getYear();
		int mes=hoy.getMonthValue();
		String mesn="";
		if(mes<10) {
			mesn="0"+mes;
		}else {
			mesn=""+mes;
		}
		
		return LocalDateTime.parse(anio+"-"+mesn+"-01T00:00:00");
	}
	
	//Retorna fecha del ultimo dia del mes actual
	public LocalDateTime lastDay() {
		
		LocalDateTime hoy = LocalDateTime.now();
		int anio=hoy.getYear();
		int mes=hoy.getMonthValue();
		Calendar calFin = Calendar.getInstance();		
		calFin.set(anio, mes-1, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		hoy = LocalDateTime.ofInstant(calFin.getTime().toInstant(),calFin.getTimeZone().toZoneId());
	    return hoy;
	}
	
	//Retorna estados de cuenta del mes actual
	public Flux<AccountState> findNumberAccount(@PathVariable String id){
		LocalDateTime fecha1=this.firstDay();
		LocalDateTime fecha2=this.lastDay();
	       System.out.print(servicestate.findAccount(fecha1,fecha2,id).collectList().block().size());
		return servicestate.findAccount(fecha1,fecha2,id);
	}

}
