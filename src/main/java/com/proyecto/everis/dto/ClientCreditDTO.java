package com.proyecto.everis.dto;

import java.util.List;

import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Credit;

import lombok.Data;

@Data
public class ClientCreditDTO {
	
	private Client client;
	
	private List<Credit> credits;

}
