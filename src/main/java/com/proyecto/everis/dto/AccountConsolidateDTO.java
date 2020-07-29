package com.proyecto.everis.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;

//DTO para listado de cuenta por bancos
@Data
public class AccountConsolidateDTO {
	
	private String tipoCuenta;
	private String descripcion;
	private Double monto;	
	@JsonSerialize(using = ToStringSerializer.class) 
	private LocalDateTime fecha_apertura;	
	private List<String> firmantes;
	private List<String> titulares;

}
