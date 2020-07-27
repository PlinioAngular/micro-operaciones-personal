package com.proyecto.everis.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.AccountState;

import reactor.core.publisher.Flux;

@Repository
public interface IAccountStateRepository extends ReactiveMongoRepository<AccountState, String>{
	
	Flux<AccountState> findByAccountId(String id);

}
