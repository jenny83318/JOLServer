package com.Jenny.JOLServer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	@Builder.Default
	private Integer cartId = 0;
	@Builder.Default
	private Integer prodId = 0;
	@Builder.Default
	private String account = "";
	@Builder.Default
	private Integer qty = 0;
	@Builder.Default
	private String size = "";
	@Builder.Default
	private String updateDt = "";
	@Builder.Default
	private boolean isCart = true;
	
	@Transient
	private String sizeInfo;
	@Transient
	private Integer price;
	@Transient
	private String prodName;
	@Transient
	private String imgUrl;
}
