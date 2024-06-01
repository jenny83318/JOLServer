package com.Jenny.JOLServer.model;

import java.time.ZonedDateTime;

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
	@Builder.Default
	private int orderNo = 0;
	@Builder.Default
	private String account = "";
	@Builder.Default
	private String email = "";
	@Builder.Default
	private int totalAmt = 0;
	@Builder.Default
	private ZonedDateTime orderTime = null;
	@Builder.Default
	private String status = "";
	@Builder.Default
	private String deliveryWay = "";
	@Builder.Default
	private String deliveryNo = "";
	@Builder.Default
	private String orderName = "";
	@Builder.Default
	private String orderPhone = "";
	@Builder.Default
	private String orderCity = "";
	@Builder.Default
	private String orderDistrict = "";
	@Builder.Default
	private String orderAddress = "";
	@Builder.Default
	private String sendName = "";
	@Builder.Default
	private String sendPhone = "";
	@Builder.Default
	private String sendCity = "";
	@Builder.Default
	private String sendDistrict = "";
	@Builder.Default
	private String sendAddress = "";
	@Builder.Default
	private String vehicleType = "";
	@Builder.Default
	private String vehicle = "";
	@Builder.Default
	private String payBy = "";
	@Builder.Default
	private ZonedDateTime updateDt = null;
}