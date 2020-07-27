package com.proyecto.everis.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "credit_state")
@Data
@ApiModel(description = "Clase de la coleción estado de crédito")
public class CreditState {
	
	@ApiModelProperty(notes = "Id de estado de crédito")
	private String id;
	
	@ApiModelProperty(notes = "Id del crédito")
	@NotNull
	private String creditId;
	
	@ApiModelProperty(notes = "Monto del movimiento realizado")
	@NotNull
	private Double monto;
	
	@ApiModelProperty(notes = "Fecha del movimiento")
	@JsonSerialize(using = ToStringSerializer.class) 
	@NotNull
	private LocalDateTime fecha;
	
	@ApiModelProperty(notes = "Descripcion del movimiento")
	@NotNull
	private String descripcion;

}
