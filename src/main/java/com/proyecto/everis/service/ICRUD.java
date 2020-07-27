package com.proyecto.everis.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T> {
	
	Mono<T> create (T t);
	Mono<T> update(T t);
	Mono<Void> delete(String id);
	Mono<T> findId(String id);
	Flux<T> listAll();
	Mono<Void> deleteAll();


}
