package com.proyecto.everis.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document( collection ="product" )
@Data
public class Product {
	
	private String id;	
	private String nameProduct;	
	private String typeProduct;

}
