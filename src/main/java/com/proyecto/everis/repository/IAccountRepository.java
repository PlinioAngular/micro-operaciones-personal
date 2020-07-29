package com.proyecto.everis.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.Account;

import reactor.core.publisher.Flux;

@Repository
public interface IAccountRepository extends ReactiveMongoRepository<Account, String> {
	
	Flux<Account> findByClientId(String id);
	Flux<Account> findByClientIdAndBankId(String idClient, String ibBank);

}
