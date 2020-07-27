package com.proyecto.everis.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Clase de referencia de cliente")
public class Client {
			
			@ApiModelProperty(notes = "ID del cliente único")
         private String id;
			@ApiModelProperty(notes = "Tipo de documento del cliente tal como: DNI, CE, RUC, etc")
         private String typeDoc;
			@ApiModelProperty(notes = "Nombre completo del cliente personal, nulo en cliente empresarial")
         private String nameClient;
			@ApiModelProperty(notes = "Nombre según registro de cliente empresarial, nulo en cliente pesrsonal")
	     private String razonSocial;
			@ApiModelProperty(notes = "Número del tipo de documento")
         private String numDoc;
			@ApiModelProperty(notes = "Tipo de cliente puede ser empresarial o personal")
         private String typeClient;
			@ApiModelProperty(notes = "Condición de cliente: personal normal o vip y empresarial mype o corporativo")
	     private String conditionClient;
		
}
