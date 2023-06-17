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
@Table(name = "jol_order_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int orderDetailNo;
	private int orderNo;
	private String account;
	private int prodId;
	private int qty;
	private int price;
	private String size;
	private String status;
	private String updateDt;
	@Transient
	private String imgUrl;
	@Transient
	private String prodName;
}