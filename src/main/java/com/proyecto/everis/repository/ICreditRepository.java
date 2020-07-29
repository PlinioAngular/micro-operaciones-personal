package com.proyecto.everis.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.Credit;

import reactor.core.publisher.Flux;

@Repository
public interface ICreditRepository extends ReactiveMongoRepository<Credit, String> {
	
	Flux<Credit> findByClientId(String id);
	
	Flux<Credit> findByClientIdAndBankId(String idClient, String ibBank);
}
