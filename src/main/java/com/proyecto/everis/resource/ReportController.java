package com.proyecto.everis.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.everis.dto.AccountConsolidateDTO;
import com.proyecto.everis.dto.AccountDTO;
import com.proyecto.everis.dto.AccountStateDTO;
import com.proyecto.everis.dto.BankAccountConsolidateDTO;
import com.proyecto.everis.dto.ClientAccountConsolidateDTO;
import com.proyecto.everis.dto.ClientAccountDTO;
import com.proyecto.everis.dto.ClientCreditDTO;
import com.proyecto.everis.dto.CreditStateDTO;
import com.proyecto.everis.model.Account;
import com.proyecto.everis.model.AccountState;
import com.proyecto.everis.model.Bank;
import com.proyecto.everis.model.Client;
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
@RequestMapping("reports")
public class ReportController {
	
	@Autowired
	private IAccountService service;
	
	@Autowired
	private ICreditService serviceCredit;
	
	@Autowired
	private IAccountStateService servicestate;
	
	@Autowired
	private ICreditStateService servicestatecredit;
	
	private MethodsAccount methods=new MethodsAccount();
	
	// ### INICIO REPORTE DE LAS CUENTAS POR CLIENTE ### //
	@ApiOperation(
            value = "Ver cuentas por cliente",
            notes = "Lista las cuentas a un cliente determinado"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/viewaccounts/{id}")
	Mono<ClientAccountDTO> listAccountByClientId(@PathVariable String id) {	
		ClientAccountDTO dto= new ClientAccountDTO();
		List<Account> lac=new ArrayList<Account>();
		List<AccountDTO> lacd=new ArrayList<AccountDTO>();
		Client cl = new Client();		
		cl=this.methods.listOneClient(id).block();
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
				adto.setBanco(methods.listOneBank(account.getBankId()).block());
				adto.setProduct(methods.listOneProduct(account.getProductId()).block());
				lacd.add(adto);			 
			 }
		});
		dto.setClient(cl);
		dto.setAccounts(lacd);
		Mono<ClientAccountDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	@ApiOperation(
            value = "Estado de cuenta",
            notes = "Lista los movimientos de cuenta por id"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/stateaccounts/{id}")
	Mono<AccountStateDTO> listStateAccountById(@PathVariable String id) {
		
		AccountStateDTO dto=new AccountStateDTO();
		List<AccountState> ls=new ArrayList<AccountState>();
		Account ac=new Account();
		ac=this.service.findId(id).block();
		ls=servicestate.findByAccountId(id).collectList().block();
		dto.setAccount(ac);
		dto.setStates(ls);
		Mono <AccountStateDTO> fdto=Mono.just(dto);
		return fdto;
	}
	
	@ApiOperation(
            value = "Estado de cuenta",
            notes = "Lista los movimientos de cuenta por id y rango de fechas"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/stateaccountdates/{id}/{sfecha1}/{sfecha2}")
	Mono<AccountStateDTO> listStateAccountByIdAndFecha(@PathVariable String id,@PathVariable String sfecha1,@PathVariable String sfecha2) {
		LocalDateTime fecha1= LocalDateTime.parse(sfecha1);
		LocalDateTime fecha2= LocalDateTime.parse(sfecha2);
		AccountStateDTO dto=new AccountStateDTO();
		List<AccountState> ls=new ArrayList<AccountState>();
		Account ac=new Account();
		ac=this.service.findId(id).block();
		ls=servicestate.findByAccountIdAndFecha(fecha1, fecha2, id).collectList().block();
		dto.setAccount(ac);
		dto.setStates(ls);
		Mono <AccountStateDTO> fdto=Mono.just(dto);
		return fdto;
	}
	
	// ### FIN REPORTE DE LAS CUENTAS POR CLIENTE ### //
	
	// ### INICIO REPORTE DE LAS CREDITOS POR CLIENTE ### //
	
	@ApiOperation(
            value = "Ver credito por cliente",
            notes = "Lista los creditos de un cliente determinado"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/viewcredits/{id}")
	Mono<ClientCreditDTO> listCreditByClientId(@PathVariable String id) {	
		ClientCreditDTO dto= new ClientCreditDTO();
		List<Credit> lac=new ArrayList<Credit>();
		Client cl = new Client();		
		cl=methods.listOneClient(id).block();
		lac=serviceCredit.findByClientId(id).collectList().block();
		dto.setClient(cl);
		dto.setCredits(lac);		
		Mono<ClientCreditDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	@ApiOperation(
            value = "Movimientos de credito",
            notes = "Lista los movimientos de cuenta por id"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/statecredits/{id}")
	Mono<CreditStateDTO> listStateCreditById(@PathVariable String id) {
		
		CreditStateDTO dto=new CreditStateDTO();
		List<CreditState> ls=new ArrayList<CreditState>();
		Credit cr=new Credit();
		cr=serviceCredit.findId(id).block();
		ls=servicestatecredit.findByCreditId(id).collectList().block();
		dto.setCredit(cr);
		dto.setStates(ls);
		Mono<CreditStateDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	@ApiOperation(
            value = "Movimientos de credito",
            notes = "Lista los movimientos de cuenta por id y rango de fechas"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/statecreditdates/{id}/{sfecha1}/{sfecha2}")
	Mono<CreditStateDTO> listStateCreditByIdAnDate(@PathVariable String id,@PathVariable String sfecha1,@PathVariable String sfecha2) {
		LocalDateTime fecha1= LocalDateTime.parse(sfecha1);
		LocalDateTime fecha2= LocalDateTime.parse(sfecha2);
		CreditStateDTO dto=new CreditStateDTO();
		List<CreditState> ls=new ArrayList<CreditState>();
		Credit cr=new Credit();
		cr=serviceCredit.findId(id).block();
		ls=servicestatecredit.findByCreditIdAndFecha(fecha1, fecha2, id).collectList().block();
		dto.setCredit(cr);
		dto.setStates(ls);
		Mono<CreditStateDTO> mdto=Mono.just(dto);
		return mdto;
	}
	
	// ### FIN REPORTE DE LAS CREDITOS POR CLIENTE ### //
	
	
	// ### INICIO REPORTE CONSOLIDADO POR CLIENTE ### //
	
	@ApiOperation(
            value = "Todo los productos",
            notes = "Lista las cuentas y credito por bancos de un cliente"
    )
	@CircuitBreaker(name="ms2", fallbackMethod = "findError")
	@TimeLimiter(name="ms2")
	@GetMapping("/oneclientconsolidates/{id}")
	Mono<ClientAccountConsolidateDTO> listBankId(@PathVariable String id){
		ClientAccountConsolidateDTO cuentaConsolidada=new ClientAccountConsolidateDTO();
		cuentaConsolidada.setNombre(methods.listOneClient(id).block().getNameClient());
		cuentaConsolidada.setDocNum(methods.listOneClient(id).block().getNumDoc());
		List<Bank> abdto= methods.listAllBank().collectList().block();
		Stream<Bank>  sab= abdto.stream();
		List<BankAccountConsolidateDTO> bccdto=new ArrayList<BankAccountConsolidateDTO>();
		sab.forEach(action->{
			BankAccountConsolidateDTO bacdto=new BankAccountConsolidateDTO();
			bacdto.setBancNom(methods.listOneBank(action.getId()).block().getNameBank());
			List<AccountConsolidateDTO> ladto= new ArrayList<AccountConsolidateDTO>();
			List<Credit> credit= new ArrayList<Credit>();
			List<Account> account= new ArrayList<Account>();
			credit=serviceCredit.findByClientIdAndBankId(id, action.getId()).collectList().block();
			account=service.findByClientIdAndBankId(id, action.getId()).collectList().block();
			
			account.stream().forEach(acc->{
				AccountConsolidateDTO adto=new AccountConsolidateDTO();
				adto.setDescripcion(acc.getDescripcion());
				adto.setTipoCuenta(methods.listOneProduct(acc.getProductId()).block().getNameProduct());
				adto.setMonto(acc.getMonto());
				adto.setFecha_apertura(acc.getFecha_apertura());
				adto.setFirmantes(acc.getFirmantes());
				adto.setTitulares(acc.getTitulares());
				ladto.add(adto);
			});
			bacdto.setCuentas(ladto);
			bacdto.setCreditos(credit);
			bccdto.add(bacdto);
			
		});
		cuentaConsolidada.setBancos(bccdto);
		return Mono.just(cuentaConsolidada);
	}
	
	// ### FIN REPORTE CONSOLIDADO POR CLIENTE ### //
	
	//Método de repsuesta del circuitbraker
	Mono<ResponseEntity<String>> findError(Exception ex){
		return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha ocurrido un error intente en unos minutos"));
	}

}
