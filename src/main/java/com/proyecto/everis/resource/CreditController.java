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

import com.proyecto.everis.model.Credit;
import com.proyecto.everis.service.ICreditService;
import com.proyecto.everis.util.ValidationCredit;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/credits")
public class CreditController {
	
	@Autowired
	private ICreditService service;
	
	private ValidationCredit validation= new ValidationCredit();
	
	//### Inicio CRUD de la clase  crédito ###
	@ApiOperation(
            value = "Agrega cuenta",
            notes = "El parámetro de de tipo Credit"
    )
	@PostMapping()
	Mono<Credit> create(@Valid @RequestBody Credit Credit) {
		if(validation.registerAccount(Credit.getClientId(), Credit.getProductId(), Credit.getBankId())==true) {
			return service.create(Credit);
		}else {
			return null;
		}
	}
	
	@ApiOperation(
            value = "Actualizar cuenta",
            notes = "El parámetro de de tipo Credit"
    )
	@PutMapping()
	Mono<Credit> update(@Valid @RequestBody Credit Credit) {
		return this.service.update(Credit);
	}
	
	@ApiOperation(
            value = "Listar toda cuenta",
            notes = "No necesita parámetros"
    )
	@GetMapping(produces="application/json")
	Flux<Credit> list() {
		return service.listAll();
	}
	
	@ApiOperation(
            value = "Lista una cuentapor id",
            notes = "El parámetro es de tipo string"
    )
	@GetMapping("/{id}")
	Mono<Credit> findById(@PathVariable String id) {
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
