package com.proyecto.everis.resource;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.model.Credit;
import com.proyecto.everis.model.CreditState;
import com.proyecto.everis.service.IAccountService;
import com.proyecto.everis.service.IAccountStateService;
import com.proyecto.everis.service.ICreditService;
import com.proyecto.everis.service.ICreditStateService;
import com.proyecto.everis.util.MethodsAccount;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("operations")
public class OperationsController {
	
	@Autowired
	private IAccountService service;
	
	@Autowired
	private IAccountStateService servicestate;
	
	@Autowired
	private ICreditService serviceCredit;
	
	@Autowired
	private ICreditStateService servicestatecredit;
	
	private MethodsAccount methods=new MethodsAccount();
	
	//### Inicio de las operaciones de cuenta ###
		
		@ApiOperation(
	            value = "Movimientos de cuenta",
	            notes = "Resgistra los movimientos de cuenta, retiro o depósito"
	    )
		@CircuitBreaker(name="ms2", fallbackMethod = "findError")
		@TimeLimiter(name="ms2")
		@PostMapping("/updateaccounts")
		Mono<Account> updateAccount(HttpServletRequest request) {
			Double cambio=Double.parseDouble(request.getParameter("monto"));
			String id=request.getParameter("id");
			int cantOperaciones=methods.findNumberAccount(id).collectList().block().size();
			Account acc=new Account();
			LocalDateTime fecha = LocalDateTime.now();
			acc=service.findId(id).block();
			if(cantOperaciones<11) {
				acc.setMonto(acc.getMonto()+cambio);
			}else {
				acc.setMonto(acc.getMonto()+cambio-10);
				AccountState as=new AccountState();		
				as.setAccountId(id);
				as.setMonto(10.0);
				as.setFecha(fecha);
				as.setDescripcion("Comision");
				this.servicestate.create(as).subscribe();
			}
			
			AccountState as=new AccountState();		
			as.setAccountId(id);
			as.setMonto(cambio);
			as.setFecha(fecha);
			as.setDescripcion(request.getParameter("descripcion"));
			this.servicestate.create(as).subscribe();
			return this.service.update(acc);
		}
		
		@ApiOperation(
	            value = "Transferencia",
	            notes = "Realiza y registra las transferencias entre cuentas"
	    )
		@CircuitBreaker(name="ms2", fallbackMethod = "findError")
		@TimeLimiter(name="ms2")
		@PostMapping("/transferaccounts")
		Mono<Account> transferAccount(HttpServletRequest request) {
			Double cambio=Double.parseDouble(request.getParameter("monto"));
			String id=request.getParameter("id");
			String idCargo=request.getParameter("idCargo");
			Account acc=new Account();
			Account acargo=new Account();
			LocalDateTime fecha = LocalDateTime.now();
			acc=service.findId(id).block();
			acargo=service.findId(idCargo).block();
			if(acargo.getMonto()-cambio<0) {
				acc.setMonto(acc.getMonto()+cambio);
				acargo.setMonto(acargo.getMonto()-cambio);
				AccountState as=new AccountState();
				as.setAccountId(id);
				as.setMonto(cambio);
				as.setFecha(fecha);
				as.setDescripcion("Ingreso por transferencia");
				as.setDescripcion(request.getParameter("descripcion"));		
				AccountState asCargo=new AccountState();
				asCargo.setAccountId(idCargo);
				asCargo.setMonto(-cambio);
				asCargo.setFecha(fecha);
				asCargo.setDescripcion("Egreso por transferencia");
				this.servicestate.create(as).subscribe();
				this.servicestate.create(asCargo).subscribe();
				this.service.update(acc).subscribe();
				return this.service.update(acargo);
			}else {
				return null;
			}
			
		}
		
		@ApiOperation(
	            value = "Pago de credito",
	            notes = "Resgistra y realiza el pago de creditos desde una cuenta"
	    )
		@CircuitBreaker(name="ms2", fallbackMethod = "findError")
		@TimeLimiter(name="ms2")
		@PostMapping("/paycredits")
		Mono<Account> payCredit(HttpServletRequest request) {
			Double cambio=Double.parseDouble(request.getParameter("monto"));
			String idCargo=request.getParameter("idCargo");
			String idCredit=request.getParameter("idCredit");
			Account acc=new Account();
			Credit cr=new Credit();
			LocalDateTime fecha = LocalDateTime.now();
			acc=service.findId(idCargo).block();
			cr=serviceCredit.findId(idCredit).block();
			acc.setMonto(acc.getMonto()-cambio);
			cr.setConsumido(cr.getConsumido()-cambio);
			AccountState as=new AccountState();		
			as.setAccountId(idCargo);
			as.setMonto(-cambio);
			as.setFecha(fecha);
			as.setDescripcion(request.getParameter("descripcion"));
			CreditState cs=new CreditState();		
			cs.setCreditId(idCredit);
			cs.setMonto(-cambio);
			cs.setFecha(fecha);
			cs.setDescripcion(request.getParameter("descripcion"));
			this.servicestate.create(as).subscribe();
			this.servicestatecredit.create(cs).subscribe();
			this.serviceCredit.update(cr);
			return this.service.update(acc);
		}
		
		//### Fin de las operaciones de las cuentas
		
		//### Inicio de las operaciones de credito ###
		
	
		
		@ApiOperation(
	            value = "Registro de movimientos de TC",
	            notes = "También actualiza la cuenta acorde al movimiento"
	    )
		@CircuitBreaker(name="ms2", fallbackMethod = "findError")
		@TimeLimiter(name="ms2")
		@GetMapping("/updatecredits/{id}/{monto}")
		Mono<Credit> updateCredit(@PathVariable String id,@PathVariable String monto) {
			Double cambio=Double.parseDouble(monto);
			Credit cr=new Credit();
			LocalDateTime fecha = LocalDateTime.now();
			cr=serviceCredit.findId(id).block();	
			if(cr.getConsumido()+cambio < cr.getTotal_credito()) {
				cr.setConsumido(cr.getConsumido()+cambio);
				
				CreditState as=new CreditState();		
				as.setCreditId(id);
				as.setMonto(cambio);
				as.setFecha(fecha);
				servicestatecredit.create(as).subscribe();
				System.out.print(as.toString());
				return this.serviceCredit.update(cr);
			}else {
				return null;
			}
		}
		
		//### Fin de las operaciones de credito
		
		//Método de repsuesta del circuitbraker
		Mono<ResponseEntity<String>> findError(Exception ex){
			return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha ocurrido un error intente en unos minutos"));
		}
}
