package com.proyecto.everis.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.CreditState;

import reactor.core.publisher.Flux;

@Repository
public interface ICreditStateRepository extends ReactiveMongoRepository<CreditState, String> {
	
	Flux<CreditState> findByCreditId(String id);

}
