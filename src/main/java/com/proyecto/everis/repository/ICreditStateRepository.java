package com.proyecto.everis.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.everis.model.CreditState;

import reactor.core.publisher.Flux;

@Repository
public interface ICreditStateRepository extends ReactiveMongoRepository<CreditState, String> {
	
	Flux<CreditState> findByCreditId(String id);
	
	@Query("{fecha:{$gt:?0,$lt:?1},accountId:?2}")
	Flux<CreditState> findByCreditIdAndFecha(LocalDateTime fecha1,LocalDateTime fecha2, String id);

}
