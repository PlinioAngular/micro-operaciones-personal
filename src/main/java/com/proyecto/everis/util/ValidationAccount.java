package com.proyecto.everis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.dto.AccountDTO;
import com.proyecto.everis.dto.ClientAccountDTO;
import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Credit;
import com.proyecto.everis.model.Product;

//Clase para validación de datos en registro de cuentas
public class ValidationAccount {
	
	private int contador=0;
	
	//Verifica si no cumple las condiciones mencionadas lineas infra
	public boolean registerAccount(Account account) {
		boolean condicion=true;
		if(!this.bankExist(account.getBankId())) {
			condicion =false;
		}
		if(!this.clientExist(account.getClientId())) {
			condicion =false;
		}else {
			if(!this.isPersonal(account.getClientId())) {
				condicion =false;
			}
		}
		if(!this.productExist(account.getProductId())) {
			condicion =false;
		}else {
			if(!this.isAccount(account.getProductId())) {
				condicion =false;
			}
		}
		if(!this.listClient(account.getClientId(), account.getProductId(),account.getBankId(),account)) {
			condicion=false;
		}
		if(!this.statusCredit(account.getClientId())) {
			condicion=false;
		}
		return condicion;
		
	}
	
	//Valida si el producto es una cuenta
	private boolean isAccount(String id) {
		boolean condicion=true;
		WebClient webClient=WebClient.create("http://localhost:8099/micro-crud");
		if(webClient.get().uri("/products/"+id).retrieve().bodyToMono(Product.class).block().getTypeProduct().equals("CUENTA")) {
			condicion = true;
		}else {
			condicion = false;
			System.out.print("Error: No es una cuenta \n");
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
			System.out.print("Error: No es un cliente personal \n");
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
	
	//Valida si existe una deuda de credito sin pagar
	private boolean statusCredit(String id) {
		boolean condicion=true;
		List<Credit> credit=new ArrayList<Credit>();
		Client client=new Client();
		WebClient webClient=WebClient.create("http://localhost:8099");
		client=webClient.get().uri("/micro-crud/clients/"+id).retrieve().bodyToMono(Client.class).block();
		credit=webClient.get().uri("/micro-operaciones-personal/credits/viewcredits/"+id).retrieve().bodyToFlux(Credit.class).collectList().block();
		Stream<Credit> crs=credit.stream();
		crs.forEach(cre->{
			if(cre.isStatusCredit()) {
				contador ++;
				System.out.print("Error: Tiene una deuda pendiente \n");
				return;
			}
		});
		if(contador>0) {
			condicion=false;
		}
		
		return condicion;
	}
	
	//Valida si tiene la cantidad de cuentas especificadas
	private boolean listClient(String id,String idProduct,String idBanco, Account account) {
		
		boolean condicion=true;
		Client client=new Client();
		Product product=new Product();
		ClientAccountDTO lac=new ClientAccountDTO();		
		WebClient webClient=WebClient.create("http://localhost:8099");
		client=webClient.get().uri("/micro-crud/clients/"+id).retrieve().bodyToMono(Client.class).block();
		product=webClient.get().uri("/micro-crud/products/"+idProduct).retrieve().bodyToMono(Product.class).block();
		lac=webClient.get().uri("/micro-operaciones-personal/reports/viewaccounts/"+id).retrieve().bodyToMono(ClientAccountDTO.class).block();
		Stream< AccountDTO> acst=lac.getAccounts().stream();
		if(client.getTypeClient().equals("PERSONAL") && product.getTypeProduct().equals("CUENTA")) {
			if(client.getConditionClient().equals("VIP") && account.getMonto()<1000.00) {
				System.out.print("Error: El monto mínimo es 1000 \n");
				contador ++;
			}
			acst.forEach(action->{
				if(action.getProduct().getId().equals(idProduct)) {
					if(action.getBanco().getId().equals(idBanco)) {
						System.out.print("Error: Solo puede tener un tipo de cuenta \n");
						contador ++;
						return;
					}
					
				}
			});
					
		}
		if(client.getTypeClient().equals("EMPRESARIAL")) {
			if(product.getTypeProduct().equals("CUENTA") && !product.getNameProduct().equals("CORRIENTE"))
				System.out.print("Error: No puede tener el tipo de cuenta escogido \n");
			contador++;
		}
		if(contador>0) {
			condicion =false;
		}	
		contador=0;
		return condicion;
	}

}
