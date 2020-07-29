package com.proyecto.everis.service;
import com.proyecto.everis.model.Credit;

import reactor.core.publisher.Flux;

public interface ICreditService extends ICRUD<Credit> {
	
	Flux<Credit> findByClientId(String id);
	Flux<Credit> findByClientIdAndBankId(String idClient, String ibBank);

}
