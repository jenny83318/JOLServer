package com.Jenny.JOLServer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jenny.JOLServer.model.Customer;

@Repository
public interface CustomerInfoDao extends JpaRepository<Customer, Long>{

}
