package com.proyecto.everis.service;

import com.proyecto.everis.model.CreditState;

import reactor.core.publisher.Flux;

public interface ICreditStateService extends ICRUD<CreditState> {
	
	Flux<CreditState> findByCreditId(String id);

}
