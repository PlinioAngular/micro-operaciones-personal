package com.proyecto.everis.service;

import com.proyecto.everis.model.Account;

import reactor.core.publisher.Flux;


public interface IAccountService extends ICRUD<Account> {
	
	Flux<Account> findByClientId(String id);
	Flux<Account> findByClientIdAndBankId(String idClient, String ibBank);

}
