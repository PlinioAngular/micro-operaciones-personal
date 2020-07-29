package com.proyecto.everis.dto;

import java.util.List;

import lombok.Data;

//DTO para el consolidado de productos de bancos por cliente
@Data
public class ClientAccountConsolidateDTO {
	
	private String nombre;
	private String docNum;
	private List<BankAccountConsolidateDTO> bancos;

}
