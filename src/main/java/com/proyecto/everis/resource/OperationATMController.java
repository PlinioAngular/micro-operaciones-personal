package com.proyecto.everis.resource;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.model.Bank;
import com.proyecto.everis.service.IAccountService;
import com.proyecto.everis.service.IAccountStateService;
import com.proyecto.everis.util.MethodsAccount;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("atmoperations")
public class OperationATMController {
	
	@Autowired
	private IAccountService service;
	
	@Autowired
	private IAccountStateService servicestate;
	
	private MethodsAccount methods=new MethodsAccount();
	
	@ApiOperation(
            value = "Movimientos de cuenta",
            notes = "Resgistra los movimientos de cuenta, retiro o depósito"
    )
	@CircuitBreaker(name="ms2")
	@TimeLimiter(name="ms2")
	@PostMapping("/updateaccounts")
	Mono<Account> updateAccount(HttpServletRequest request) {
		Double cambio=Double.parseDouble(request.getParameter("monto"));
		String id=request.getParameter("id");
		String idBanco=request.getParameter("idBanco");
		String tipOper=request.getParameter("tipOper");
		int cantOpeEjec=0;
		if(!tipOper.equals("DEPOSITO")) {
			cambio= -1 * cambio;
		}
		Bank bank=methods.listOneBank(idBanco).block();
		cantOpeEjec=methods.findNumberAccountState(id, tipOper, idBanco).collectList().block().size();
		
		int cantOpePerm=0;
		Double comision=0.0;
		Account acc=new Account();
		LocalDateTime fecha = LocalDateTime.now();
		acc=service.findId(id).block();
		if(bank.getId().equals(acc.getBankId())) {
			cantOpePerm=bank.getCantOpeInter();	
			comision = bank.getComInter();
		}else {
			cantOpePerm=bank.getCantOpeExter();	
			comision = bank.getComExter();
		}
		if(cantOpeEjec<=cantOpePerm) {
			acc.setMonto(acc.getMonto()+cambio);
		}else {
			acc.setMonto(acc.getMonto()-comision);
			acc=this.service.update(acc).block();
			acc.setMonto(acc.getMonto()+cambio);
			AccountState as=new AccountState();		
			as.setAccountId(id);
			as.setMonto(10.0);
			as.setFecha(fecha);
			as.setDescripcion("Comision");
			as.setBancOpe(idBanco);
			as.setTipoMovimiento("COMISION");
			this.servicestate.create(as).subscribe();
		}
		
		AccountState as=new AccountState();		
		as.setAccountId(id);
		as.setMonto(cambio);
		as.setFecha(fecha);
		as.setDescripcion(request.getParameter("descripcion"));
		as.setBancOpe(idBanco);
		as.setTipoMovimiento(tipOper);
		this.servicestate.create(as).subscribe();
		return this.service.update(acc);
	}

}
