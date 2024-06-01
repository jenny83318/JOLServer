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
@Table(name = "jol_product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer prodId;
	private String name;
	private String descript;
	private String imgUrl;
	private ZonedDateTime updateDt;
	private ZonedDateTime createDt;
	private Integer price;
	private Integer cost;
	private Integer qty;
	private String category;
	private String series;
	private String sizeInfo;
}