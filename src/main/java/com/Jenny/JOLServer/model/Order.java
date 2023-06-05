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
@Table(name = "jol_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int orderNo;
	private String account;
	private String email;
	private int totalAmt;
	private String orderTime;
	private String status;
	private String deliveryWay;
	private String deliveryNo;
	private String orderName;
	private String orderPhone;
	private String orderCity;
	private String orderDistrict;
	private String orderAddress;
	private String sendName;
	private String sendPhone;
	private String sendCity;
	private String sendDistrict;
	private String sendAddress;
	private String vehicleType;
	private String vehicle;
	private String payBy;	
	private String updateDt;
}