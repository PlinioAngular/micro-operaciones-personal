package com.proyecto.everis.service;

import java.time.LocalDateTime;

import com.proyecto.everis.model.AccountState;

import reactor.core.publisher.Flux;

public interface IAccountStateService extends ICRUD<AccountState>{
	
	Flux<AccountState> findByAccountId(String id);
	
	Flux<AccountState> findAccount(LocalDateTime fecha1,LocalDateTime fecha2,String id);
	
	Flux<AccountState> findByAccountIdAndFecha(LocalDateTime fecha1,LocalDateTime fecha2, String id);

}
