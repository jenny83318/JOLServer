package com.Jenny.JOLServer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jenny.JOLServer.model.Customer;



@Repository
public interface JOLCustomerInfoDao extends JpaRepository<Customer, Integer>{
	
	 List<Customer> findAll();
	 

}


