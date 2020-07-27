package com.proyecto.everis.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.service.IAccountStateService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stateaccounts")
public class AccountStateController {
	
	
	@Autowired
	private IAccountStateService service;
	
	@PostMapping()
	Mono<AccountState> create(@Valid @RequestBody AccountState ClientsStream) {
		return this.service.create(ClientsStream);
	}
	
	@PutMapping()
	Mono<AccountState> update(@Valid @RequestBody AccountState ClientsStream) {
		return this.service.update(ClientsStream);
	}

	@GetMapping(produces="application/json")
	Flux<AccountState> list() {
		return service.listAll();
	}

	@GetMapping("/{id}")
	Mono<AccountState> findById(@PathVariable String id) {
		return this.service.findId(id);
	}
	
	@DeleteMapping("/{id}")
	Mono<Void> deleteById(@PathVariable String id) {
		return this.service.delete(id);
	}
	
	@DeleteMapping
	Mono<Void> deleteAll() {
		return this.service.deleteAll();
	}
}
