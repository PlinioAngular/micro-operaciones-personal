package com.proyecto.everis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.everis.model.Account;
import com.proyecto.everis.repository.IAccountRepository;
import com.proyecto.everis.service.IAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	private IAccountRepository repository;

	@Override
	public Mono<Account> create(Account t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Account> update(Account t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repository.deleteById(id);
	}

	@Override
	public Mono<Account> findId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Flux<Account> listAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Mono<Void> deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	@Override
	public Flux<Account> findByClientId(String id) {
		// TODO Auto-generated method stub
		return repository.findByClientId(id);
	}

	@Override
	public Flux<Account> findByClientIdAndBankId(String idClient, String ibBank) {
		// TODO Auto-generated method stub
		return repository.findByClientIdAndBankId(idClient, ibBank);
	}

}
