package com.proyecto.everis.dto;

import java.util.List;

import com.proyecto.everis.model.Credit;

import lombok.Data;

//DTO para listado de cuentas  y creditos por banco
@Data
public class BankAccountConsolidateDTO {
	
	private String bancNom;
	private List<AccountConsolidateDTO> cuentas;
	private List<Credit> creditos;

}
