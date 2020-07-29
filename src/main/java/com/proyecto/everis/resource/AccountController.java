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

import com.proyecto.everis.model.Account;
import com.proyecto.everis.service.IAccountService;
import com.proyecto.everis.util.ValidationAccount;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private IAccountService service;
	
	private ValidationAccount validation= new ValidationAccount();;
	
	
	//### Inicio CRUD de la clase  cuenta ###
	@ApiOperation(
            value = "Agrega cuenta",
            notes = "El parámetro de de tipo Account"
    )
	@PostMapping()
	Mono<Account> create(@Valid @RequestBody Account account) {
		if(validation.registerAccount(account)==true) {
			return service.create(account);
		}else {
			return null;
		}
		
	}
	
	@ApiOperation(
            value = "Actualizar cuenta",
            notes = "El parámetro de de tipo Account"
    )
	@PutMapping()
	Mono<Account> update(@Valid @RequestBody Account account) {
		return this.service.update(account);
	}
	
	@ApiOperation(
            value = "Listar toda cuenta",
            notes = "No necesita parámetros"
    )
	@GetMapping(produces="application/json")
	Flux<Account> list() {
		return service.listAll();
	}
	
	@ApiOperation(
            value = "Lista una cuentapor id",
            notes = "El parámetro es de tipo string"
    )
	@GetMapping("/{id}")
	Mono<Account> findById(@PathVariable String id) {
		return this.service.findId(id);
	}
	
	@ApiOperation(
            value = "Eliminda una cuenta por id",
            notes = "El parámetro es de tipo string"
    )
	@DeleteMapping("/{id}")
	Mono<Void> deleteById(@PathVariable String id) {
		return this.service.delete(id);
	}
	
	@ApiOperation(
            value = "Elimina toda cuenta",
            notes = "Utilizado para pruebas"
    )
	@DeleteMapping
	Mono<Void> deleteAll() {
		return this.service.deleteAll();
	}
	
	//### Fin del crud ###
	
	
	

}
