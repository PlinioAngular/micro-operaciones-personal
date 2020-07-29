package com.proyecto.everis.dto;

import java.util.List;
import com.proyecto.everis.model.Client;

import lombok.Data;

//DTO para listado de cuenta por cientes
@Data
public class ClientAccountDTO {
	
	private Client client;
	
	private List<AccountDTO> accounts;

}