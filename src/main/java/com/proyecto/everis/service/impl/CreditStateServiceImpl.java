package com.proyecto.everis.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.everis.model.CreditState;
import com.proyecto.everis.repository.ICreditStateRepository;
import com.proyecto.everis.service.ICreditStateService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditStateServiceImpl implements ICreditStateService {

	@Autowired
	private ICreditStateRepository repository;
	@Override
	public Mono<CreditState> create(CreditState t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<CreditState> update(CreditState t) {
		// TODO Auto-generated method stub
		return repository.save(t);
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repository.deleteById(id);
	}

	@Override
	public Mono<CreditState> findId(String id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public Flux<CreditState> listAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Mono<Void> deleteAll() {
		// TODO Auto-generated method stub
		return repository.deleteAll();
	}

	@Override
	public Flux<CreditState> findByCreditId(String id) {
		// TODO Auto-generated method stub
		return repository.findByCreditId(id);
	}

	@Override
	public Flux<CreditState> findByCreditIdAndFecha(LocalDateTime fecha1, LocalDateTime fecha2, String id) {
		// TODO Auto-generated method stub
		return repository.findByCreditIdAndFecha(fecha1, fecha2, id);
	}
	
}
