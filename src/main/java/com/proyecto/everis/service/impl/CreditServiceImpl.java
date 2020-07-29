package com.proyecto.everis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.everis.model.Credit;
import com.proyecto.everis.repository.ICreditRepository;
import com.proyecto.everis.service.ICreditService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements ICreditService {

	@Autowired
	private ICreditRepository repository;
	
	@Override
	public Mono<Credit> create(Credit t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Credit> update(Credit t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repository.deleteById(id);
	}

	@Override
	public Mono<Credit> findId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Flux<Credit> listAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Mono<Void> deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	@Override
	public Flux<Credit> findByClientId(String id) {
		// TODO Auto-generated method stub
		return repository.findByClientId(id);
	}

	@Override
	public Flux<Credit> findByClientIdAndBankId(String idClient, String ibBank) {
		// TODO Auto-generated method stub
		return repository.findByClientIdAndBankId(idClient, ibBank);
	}


}
