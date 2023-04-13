package com.Jenny.JOLServer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JOL_CustomerInfo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	@Id
	private String account;
	private String password;
	private String name;
	private String phone;
	private String email;
	private String address;
	private Integer status;
}