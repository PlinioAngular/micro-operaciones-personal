package com.proyecto.everis.dto;

import java.util.List;

import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Credit;

import lombok.Data;

//DTO para listado de creditos por cliente
@Data
public class ClientCreditDTO {
	
	private Client client;
	
	private List<Credit> credits;

}
