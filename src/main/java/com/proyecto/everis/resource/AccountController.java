package com.proyecto.everis.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto.everis.dto.AccountDTO;
import com.proyecto.everis.dto.AccountStateDTO;
import com.proyecto.everis.dto.ClientAccountDTO;
import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.model.Bank;
import com.proyecto.everis.model.Client;
import com.proyecto.everis.model.Product;
import com.proyecto.everis.service.IAccountService;
import com.proyecto.everis.service.IAccountStateService;
import com.proyecto.everis.util.Validation;

import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/personalaccounts")
public class AccountController {
	
	@Autowired
	private IAccountService service;
	
	private Validation validation= new Validation();;
	
	@Autowired
	private IAccountStateService servicestate;
	
	//### Inicio CRUD de la clase  cuenta ###
	@ApiOperation(
            value = "Agrega cuenta",
            notes = "El parámetro de de tipo Account"
    )
	@PostMapping()
	Mono<Account> create(@Valid @RequestBody Account account) {
		if(validation.registerAccount(account.getClientId(), account.getProductId(), account.getBankId())==true) {
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
	
	
	//### Inicio de las operaciones de cuenta ###
	
	@ApiOperation(
            value = "Lista TODO cliente desde MS-CRUD",
            notes = "Consume la API de MICRO-CRUD"
    )
	@GetMapping("/viewclient")
	public Flux<Client> listAllClient() {
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
	public Mono<Client> listOneClient(@PathVariable String id) {
		Mono<Client> client;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/clients/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			client=webClient.get().uri("/clients/"+id)
					.retrieve().bodyToMono(Client.class);
		}else {
			client=null;
		}	
		
		return client;
	}
	
	@GetMapping("/viewproduct")
	public Flux<Product> listAllProduct() {
		WebClient webClient=WebClient.create("http://localhost:8080");
		Flux<Product> product=webClient.get().uri("/products")
				.retrieve().bodyToFlux(Product.class);
		
		return product;
	}
	
	@ApiOperation(
            value = "Lista un cliente por id desde MS-CRUD",
            notes = "Consume la API de MICRO-CRUD"
    )
	@GetMapping("/viewoneproduct/{id}")
	public Mono<Product> listOneProduct(@PathVariable String id) {
		Mono<Product> product;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/products/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			product=webClient.get().uri("/products/"+id)
					.retrieve().bodyToMono(Product.class);
		}else {
			product=null;
		}	
		
		return product;
	}
	
	@GetMapping("/viewonebank/{id}")
	public Mono<Bank> listOneBank(@PathVariable String id) {
		Mono<Bank> bank;
		WebClient webClient=WebClient.create("http://localhost:8080/");
		if(webClient.get().uri("/banks/"+id).exchange().block().statusCode()!=HttpStatus.NOT_FOUND) {
			bank=webClient.get().uri("/banks/"+id)
					.retrieve().bodyToMono(Bank.class);
		}else {
			bank=null;
		}	
		
		return bank;
	}
	
	@ApiOperation(
            value = "Registro de movimientos de cuenta",
            notes = "También actualiza la cuenta acorde al movimiento"
    )
	@GetMapping("/updateaccounts/{id}/{monto}")
	Mono<Account> updateAccount(@PathVariable String id,@PathVariable String monto) {
		Double cambio=Double.parseDouble(monto);
		Account acc=new Account();
		LocalDateTime fecha = LocalDateTime.now();
		acc=service.findId(id).block();
		acc.setMonto(acc.getMonto()+cambio);
		AccountState as=new AccountState();		
		as.setAccountId(id);
		as.setMonto(cambio);
		as.setFecha(fecha);
		this.servicestate.create(as).subscribe();
		System.out.print(as.toString());
		return this.service.update(acc);

	}
	
	@GetMapping("/viewaccounts/{id}")
	Mono<ClientAccountDTO> listAccountByClientId(@PathVariable String id) {	
		ClientAccountDTO dto= new ClientAccountDTO();
		List<Account> lac=new ArrayList<Account>();
		List<AccountDTO> lacd=new ArrayList<AccountDTO>();
		Client cl = new Client();		
		cl=this.listOneClient(id).block();
		lac=service.findByClientId(id).collectList().block();
		lac.forEach(new Consumer<Account>() {
			@Override
			 public void accept(final Account account) {
				AccountDTO adto= new AccountDTO();
				adto.setDescripcion(account.getDescripcion());
				adto.setFecha_apertura(account.getFecha_apertura());
				adto.setFirmantes(account.getFirmantes());
				adto.setMonto(account.getMonto());
				adto.setTitulares(account.getTitulares());
				adto.setBanco(listOneBank(account.getBankId()).block());
				adto.setProduct(listOneProduct(account.getProductId()).block());
				lacd.add(adto);
			 
			 }
		});
		dto.setClient(cl);
		dto.setAccounts(lacd);
		Mono<ClientAccountDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	
	
	@GetMapping("/stateaccounts/{id}")
	Mono<AccountStateDTO> listStateAccountById(@PathVariable String id) {
		
		AccountStateDTO dto=new AccountStateDTO();
		List<AccountState> ls=new ArrayList<AccountState>();
		Account ac=new Account();
		ac=this.findById(id).block();
		ls=servicestate.findByAccountId(id).collectList().block();
		dto.setAccount(ac);
		dto.setStates(ls);
		Mono <AccountStateDTO> fdto=Mono.just(dto);
		return fdto;
	}
	
	//### Fin de las operaciones de las cuentas

}
