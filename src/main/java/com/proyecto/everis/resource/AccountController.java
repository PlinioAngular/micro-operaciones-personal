package com.proyecto.everis.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
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
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@PutMapping()
	Mono<Account> update(@Valid @RequestBody Account account) {
		return this.service.update(account);
	}
	
	@ApiOperation(
            value = "Listar toda cuenta",
            notes = "No necesita parámetros"
    )
	@CircuitBreaker(name="ms2")
	@TimeLimiter(name="ms2")
	@GetMapping(produces="application/json")
	Flux<Account> list() {
		return service.listAll();
	}
	
	@ApiOperation(
            value = "Lista una cuenta por id",
            notes = "El parámetro es de tipo string"
    )
	@CircuitBreaker(name="micro-crud", fallbackMethod = "findError")
	@TimeLimiter(name="micro-crud")
	@GetMapping("/{id}")
	Mono<Account> findById(@PathVariable String id) {
		return this.service.findId(id);
	}
	
	@ApiOperation(
            value = "Eliminda una cuenta por id",
            notes = "El parámetro es de tipo string"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@DeleteMapping("/{id}")
	Mono<Void> deleteById(@PathVariable String id) {
		return this.service.delete(id);
	}
	
	@ApiOperation(
            value = "Elimina toda cuenta",
            notes = "Utilizado para pruebas"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@DeleteMapping
	Mono<Void> deleteAll() {
		return this.service.deleteAll();
	}
	//### Fin del crud ###
	
	//Método de repsuesta del circuitbraker
	Mono<ResponseEntity<String>> findError(Exception ex){
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha ocurrido un error intente en unos minutos"));
	}
	

}
