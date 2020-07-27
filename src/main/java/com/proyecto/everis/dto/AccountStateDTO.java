package com.proyecto.everis.dto;


import java.util.List;

import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;

import lombok.Data;

@Data
public class AccountStateDTO {
	
	private Account account;
	
	private List<AccountState> states;

}
