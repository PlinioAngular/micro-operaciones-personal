package com.proyecto.everis.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Product;
// Clase para validación de datos en registro de creditos
public class ValidationCredit {
	
	//Verifica si no cumple las condiciones mencionadas lineas infra
	public boolean registerAccount(String idClient,String idProduct, String idBanco) {
		boolean condicion=true;
		if(!this.bankExist(idBanco)) {
			condicion =false;
		}
		if(!this.clientExist(idClient)) {
			if(!this.isPersonal(idClient)) {
				condicion =false;
			}
		}
		if(!this.productExist(idProduct)) {
			if(!this.isAccount(idProduct)) {
				condicion =false;
			}
		}
		return condicion;
		
	}
	
	//Valida si el producto es un credito
	private boolean isAccount(String id) {
		boolean condicion=true;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/products/"+id).retrieve().bodyToMono(Product.class).block().getTypeProduct().equals("CREDITO")) {
			condicion = true;
		}else {
			condicion = false;
			System.out.print("Error: No es un credito \n");
		}
		return condicion;
	}
	
	//Valida si el cliente es personal
	private boolean isPersonal(String id) {
		boolean condicion=true;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/clients/"+id).retrieve().bodyToMono(Client.class).block().getTypeClient().equals("PERSONAL")) {
			condicion = true;
		}else {
			condicion = false;
			System.out.print("Error: No es cliente personal \n");
		}
		return condicion;
	}
	
	//Valida si existe el banco
	private boolean bankExist( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/banks/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
			System.out.print("Error: El banco no existe \n");
		}	
		
		return condicion;
	}
	
	//Valida si existe el cliente
	private boolean clientExist( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/clients/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
			System.out.print("Error: El cliente no existe \n");
		}	
		
		return condicion;
	}
	
	//Valida si existe el producto
	private boolean productExist( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/products/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
			System.out.print("Error: El producto no existe \n");
		}	
		
		return condicion;
	}

}
