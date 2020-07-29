package com.proyecto.everis.dto;


import java.util.List;

import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;

import lombok.Data;

//DTO para listado de movimientos por cuenta
@Data
public class AccountStateDTO {
	
	private Account account;
	
	private List<AccountState> states;

}
