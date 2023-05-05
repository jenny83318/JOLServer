package com.Jenny.JOLServer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jol_cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer cartId;
	private Integer prodId;
	private String account;
	private Integer qty;
	private String size;
	private String updateDt;
	private boolean isCart;
}
