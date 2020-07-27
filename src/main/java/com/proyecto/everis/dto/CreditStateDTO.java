package com.proyecto.everis.dto;

import java.util.List;

import com.proyecto.everis.model.Credit;
import com.proyecto.everis.model.CreditState;

import lombok.Data;

@Data
public class CreditStateDTO {
	
	private Credit credit;
	
	private List<CreditState> states;

}
