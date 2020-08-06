package com.proyecto.everis.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.AccountState;

import reactor.core.publisher.Flux;

@Repository
public interface IAccountStateRepository extends ReactiveMongoRepository<AccountState, String>{
	
	Flux<AccountState> findByAccountId(String id);
	
	@Query("{fecha:{$gt:?0,$lt:?1},accountId:?2}")
	Flux<AccountState> findAccount(LocalDateTime fecha1,LocalDateTime fecha2,String id);
	
	@Query("{fecha:{$gt:?0,$lt:?1},accountId:?2}")
	Flux<AccountState> findByAccountIdAndFecha(LocalDateTime fecha1,LocalDateTime fecha2, String id);
	
	@Query("{fecha:{$gt:?0,$lt:?1},accountId:?2,tipoMovimiento:?3,bancOpe:?4}")
	Flux<AccountState> findByAccountIdAndFechaAndTipoMovimiento(LocalDateTime fecha1,LocalDateTime fecha2, String id,String tipoMovimiento, String idBanco);

}
