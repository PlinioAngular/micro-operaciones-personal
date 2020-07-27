package com.proyecto.everis.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.proyecto.everis.model.Bank;
import com.proyecto.everis.model.Product;

import lombok.Data;

@Data
public class AccountDTO {
	
	private String descripcion;
	private Double monto;
	private Product product;
	private Bank banco;
	
	@JsonSerialize(using = ToStringSerializer.class) 
	private LocalDateTime fecha_apertura;
	
	private List<String> firmantes;
	private List<String> titulares;

}
