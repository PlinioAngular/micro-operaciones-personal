package com.proyecto.everis.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Document(collection = "credit_personal")
@Data
@ApiModel(description = "Clase de la colección crédito personal normal o vip")
public class Credit {
	
	@ApiModelProperty(notes = "ID del crédito único")
	@NotNull
	private String id;
	
	@ApiModelProperty(notes = "Cliente dueño del crédito")
	@NotNull
	private String clientId;
	
	@ApiModelProperty(notes = "Detalles del movimiento")
	@NotNull
	private String descripcion;
	
	@ApiModelProperty(notes = "Producto")
	@NotNull
	private String productId;
	
	@ApiModelProperty(notes = "Banco")
	@NotNull
	private String bankId;
	
	@ApiModelProperty(notes = "Fecha de apertura")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private String fecha_apertura;
	
	@ApiModelProperty(notes = "Monto total del crédito")
	@NotNull
	private Double total_credito;
	
	@ApiModelProperty(notes = "Monto consumido")
	private Double consumido;
	
	@ApiModelProperty(notes = "Estado del crédito")
	private boolean statusCredit;

}
