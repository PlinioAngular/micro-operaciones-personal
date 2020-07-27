package com.proyecto.everis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.repository.IAccountStateRepository;
import com.proyecto.everis.service.IAccountStateService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountStateServiceImpl implements IAccountStateService {
	
	@Autowired
	private IAccountStateRepository repository;

	@Override
	public Mono<AccountState> create(AccountState t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<AccountState> update(AccountState t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repository.deleteById(id);
	}

	@Override
	public Mono<AccountState> findId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Flux<AccountState> listAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Mono<Void> deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	@Override
	public Flux<AccountState> findByAccountId(String id) {
		// TODO Auto-generated method stub
		return repository.findByAccountId(id);
	}

}
