package com.proyecto.everis.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "account_state")
@Data
@ApiModel(description = "Clase de la colección estado de cuenta")
public class AccountState {
	
	@ApiModelProperty(notes = "Id de estado de cuenta")
	private String id;
	
	@ApiModelProperty(notes = "Id de la cuenta asociada al movimiento")
	@NotNull
	private String accountId;
	
	@ApiModelProperty(notes = "Cantidad del movimiento")
	@NotNull
	private Double monto;
	
	@ApiModelProperty(notes = "Fecha del movimiento")
	@JsonSerialize(using = ToStringSerializer.class) 
	@NotNull
	private LocalDateTime fecha;
	
	@ApiModelProperty(notes = "Detalles del movimiento")
	@NotNull
	private String descripcion;
	
	@ApiModelProperty(notes = "Tipo del movimiento puede ser compras, movimiento de dinero o comisión")
	@NotNull
	private String tipoMovimiento;
	
	@ApiModelProperty(notes = "Banco en el que se realiza la operacion")
	@NotNull
	private String bancOpe;

}
