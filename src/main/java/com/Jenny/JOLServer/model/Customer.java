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
@Table(name = "jol_customerinfo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer custId;
	private String account;
	private String password;
	private String idno;
	private String name;
	private String phone;
	private String email;
	private String address;
	private Integer status;
	
//	public Customer() {
//		
//	}
//	
//	public Customer(Integer custId, String account, String password, String idno, String name, String phone,
//			String email, String address, Integer status) {
//		super();
//		this.custId = custId;
//		this.account = account;
//		this.password = password;
//		this.idno = idno;
//		this.name = name;
//		this.phone = phone;
//		this.email = email;
//		this.address = address;
//		this.status = status;
//	}
//
//
//
//	public Integer getCustId() {
//		return custId;
//	}
//	public void setCustId(Integer custId) {
//		this.custId = custId;
//	}
//	public String getAccount() {
//		return account;
//	}
//	public void setAccount(String account) {
//		this.account = account;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public String getIdno() {
//		return idno;
//	}
//	public void setIdno(String idno) {
//		this.idno = idno;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
//	public String getPhone() {
//		return phone;
//	}
//	public void setPhone(String phone) {
//		this.phone = phone;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
//	public Integer getStatus() {
//		return status;
//	}
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//		builder.append("Customer [custId=");
//		builder.append(custId);
//		builder.append(", account=");
//		builder.append(account);
//		builder.append(", password=");
//		builder.append(password);
//		builder.append(", idno=");
//		builder.append(idno);
//		builder.append(", name=");
//		builder.append(name);
//		builder.append(", phone=");
//		builder.append(phone);
//		builder.append(", email=");
//		builder.append(email);
//		builder.append(", address=");
//		builder.append(address);
//		builder.append(", status=");
//		builder.append(status);
//		builder.append("]");
//		return builder.toString();
//	}
}
