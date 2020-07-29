package com.proyecto.everis.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "account_personal")
@Data
@ApiModel(description = "Clase de la colección cuenta solo personal")
public class Account{
	@ApiModelProperty(notes = "ID de la cuenta único")
	private String id;
	
	@ApiModelProperty(notes = "Detalles de la cuenta")
	@NotNull
	private String descripcion;
	
	@ApiModelProperty(notes = "Monto actual de la cuenta")
	@NotNull
	private Double monto;
	
	@ApiModelProperty(notes = "Dueño de la cuenta")
	@NotNull
	private String clientId;
	
	@ApiModelProperty(notes = "Tipo de producto")
	@NotNull
	private String productId;
	
	@ApiModelProperty(notes = "Banco")
	@NotNull
	private String bankId;
	
	@ApiModelProperty(notes = "Fecha de apertura")
	@JsonSerialize(using = ToStringSerializer.class) 
	@NotNull
	private LocalDateTime fecha_apertura;
	
	@ApiModelProperty(notes = "Lista de los firmantes autorizados, puede ser nulo")
	private List<String> firmantes;
	
	@ApiModelProperty(notes = "Lista de los titulares puede ser nulo ya que en cliente hay un titular")
	private List<String> titulares;

}