package com.proyecto.everis.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.model.Credit;
import com.proyecto.everis.model.CreditState;
import com.proyecto.everis.service.ICreditService;
import com.proyecto.everis.service.ICreditStateService;
import com.proyecto.everis.dto.ClientCreditDTO;
import com.proyecto.everis.dto.CreditStateDTO;
import com.proyecto.everis.model.Client;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/personalcredits")
public class CreditController {
	
	@Autowired
	private ICreditService service;
	
	@Autowired
	private ICreditStateService servicestate;
	
	//### Inicio CRUD de la clase  crédito ###
	@ApiOperation(
            value = "Agrega cuenta",
            notes = "El parámetro de de tipo Credit"
    )
	@PostMapping()
	Mono<Credit> create(@Valid @RequestBody Credit Credit) {
		return this.service.create(Credit);
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
	
	
	//### Inicio de las operaciones de cuenta ###
	
	@ApiOperation(
            value = "Lista TODO cliente desde MS-CRUD",
            notes = "Consume la API de MICRO-CRUD"
    )
	@GetMapping("/viewclient")
	public Flux<Client> listAllClientt() {
		WebClient webClient=WebClient.create("http://localhost:8080");
		Flux<Client> client=webClient.get().uri("/clients")
				.retrieve().bodyToFlux(Client.class);
		
		return client;
	}
	@ApiOperation(
            value = "Lista un cliente por id desde MS-CRUD",
            notes = "Consume la API de MICRO-CRUD"
    )
	@GetMapping("/viewoneclient/{id}")
	public Mono<Client> listOneClientt(@PathVariable String id) {
		WebClient webClient=WebClient.create("http://localhost:8080/");
		Mono<Client> client=webClient.get().uri("/clients/"+id)
				.retrieve().bodyToMono(Client.class);
		
		return client;
	}
	
	@ApiOperation(
            value = "Registro de movimientos de cuenta",
            notes = "También actualiza la cuenta acorde al movimiento"
    )
	@GetMapping("/updatecredits/{id}/{monto}")
	Mono<Credit> updateCredit(@PathVariable String id,@PathVariable String monto) {
		Double cambio=Double.parseDouble(monto);
		Credit cr=new Credit();
		LocalDateTime fecha = LocalDateTime.now();
		cr=service.findId(id).block();		
		cr.setConsumido(cr.getConsumido()+cambio);
		CreditState as=new CreditState();		
		as.setCreditId(id);
		as.setMonto(cambio);
		as.setFecha(fecha);
		servicestate.create(as).subscribe();
		System.out.print(as.toString());
		return this.service.update(cr);

	}
	
	@GetMapping("/viewcredits/{id}")
	Mono<ClientCreditDTO> listCreditByClientId(@PathVariable String id) {	
		ClientCreditDTO dto= new ClientCreditDTO();
		List<Credit> lac=new ArrayList<Credit>();
		Client cl = new Client();		
		cl=this.listOneClientt(id).block();
		lac=service.findByClientId(id).collectList().block();
		dto.setClient(cl);
		dto.setCredits(lac);		
		Mono<ClientCreditDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	@GetMapping("/stateCredits/{id}")
	Mono<CreditStateDTO> listStateCreditById(@PathVariable String id) {
		
		CreditStateDTO dto=new CreditStateDTO();
		List<CreditState> ls=new ArrayList<CreditState>();
		Credit ac=new Credit();
		ac=this.findById(id).block();
		ls=servicestate.findByCreditId(id).collectList().block();
		dto.setCredit(ac);
		dto.setStates(ls);
		Mono<CreditStateDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	//### Fin de las operaciones de las cuentas

}
