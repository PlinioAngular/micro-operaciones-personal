package com.proyecto.everis.service.impl;

import java.time.LocalDateTime;

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

	@Override
	public Flux<AccountState> findAccount(LocalDateTime fecha1,LocalDateTime fecha2,String id) {
		// TODO Auto-generated method stub
		return repository.findAccount(fecha1,fecha2,id);
	}

	@Override
	public Flux<AccountState> findByAccountIdAndFecha(LocalDateTime fecha1, LocalDateTime fecha2, String id) {
		// TODO Auto-generated method stub
		return repository.findByAccountIdAndFecha(fecha1, fecha2, id);
	}

}
