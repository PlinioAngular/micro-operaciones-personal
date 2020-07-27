package com.proyecto.everis.service;

import com.proyecto.everis.model.AccountState;

import reactor.core.publisher.Flux;

public interface IAccountStateService extends ICRUD<AccountState>{
	
	Flux<AccountState> findByAccountId(String id);

}
