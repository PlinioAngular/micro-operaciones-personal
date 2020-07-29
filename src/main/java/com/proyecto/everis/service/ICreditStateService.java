package com.proyecto.everis.service;

import java.time.LocalDateTime;

import com.proyecto.everis.model.CreditState;

import reactor.core.publisher.Flux;

public interface ICreditStateService extends ICRUD<CreditState> {
	
	Flux<CreditState> findByCreditId(String id);
	
	Flux<CreditState> findByCreditIdAndFecha(LocalDateTime fecha1,LocalDateTime fecha2, String id);

}
