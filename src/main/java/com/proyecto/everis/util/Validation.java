package com.proyecto.everis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.dto.AccountDTO;
import com.proyecto.everis.dto.ClientAccountDTO;
import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Product;
import com.proyecto.everis.service.IAccountService;

@Component
public class Validation {
	
	private int contador=0;
	
	public boolean registerAccount(String idClient,String idProduct, String idBanco) {
		boolean condicion=true;
		if(!this.listOneBank(idBanco)) {
			condicion =false;
		}
		if(!this.listOneClient(idClient)) {
			condicion =false;
		}
		if(!this.listOneProduct(idProduct)) {
			condicion =false;
		}		
		if(!this.listClient(idClient, idProduct,idBanco)) {
			condicion=false;
		}
		return condicion;
		
	}
	
	private boolean listOneBank( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/banks/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
		}	
		
		return condicion;
	}
	
	private boolean listOneClient( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/clients/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
		}	
		
		return condicion;
	}
	
	private boolean listOneProduct( String id) {
		
		boolean condicion=false;;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/products/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			condicion=true;
		}else {
			condicion=false;
		}	
		
		return condicion;
	}
	
	private boolean listClient(String id,String idProduct,String idBanco) {
		
		boolean condicion=true;
		int cont =0;
		Client client=new Client();
		Product product=new Product();
		ClientAccountDTO lac=new ClientAccountDTO();		
		WebClient webClient=WebClient.create("http://localhost:8099");
		client=webClient.get().uri("/micro-crud/clients/"+id).retrieve().bodyToMono(Client.class).block();
		product=webClient.get().uri("/micro-crud/products/"+idProduct).retrieve().bodyToMono(Product.class).block();
		lac=webClient.get().uri("/micro-operaciones-personal/personalaccounts/viewaccounts/"+id).retrieve().bodyToMono(ClientAccountDTO.class).block();
		Stream< AccountDTO> acst=lac.getAccounts().stream();
		if(client.getTypeClient().equals("PERSONAL") && product.getTypeProduct().equals("CUENTA")) {
			acst.forEach(action->{
				if(action.getProduct().getId().equals(idProduct)) {
					if(action.getBanco().getId().equals(idBanco)) {
						contador ++;
						return;
					}
					
				}
			});
					
		}
		if(client.getTypeClient().equals("EMPRESARIAL")) {
			if(product.getTypeProduct().equals("CUENTA") && !product.getNameProduct().equals("CORRIENTE"))
			contador++;
		}
		if(contador>0) {
			condicion =false;
		}	
		contador=0;
		return condicion;
	}

}
