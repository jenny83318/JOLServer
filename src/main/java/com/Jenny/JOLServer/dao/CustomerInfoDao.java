package com.Jenny.JOLServer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Jenny.JOLServer.model.Customer;

@Repository
@Transactional
public interface CustomerInfoDao extends JpaRepository<Customer, Integer> {
	public static final String DELETE_BY_ACCOUNT = "delete from jol_customer where account = :account";
	
	Customer findByAccount(String account);
	
    @Modifying
    @Query(value = DELETE_BY_ACCOUNT, nativeQuery = true)
    void deleteByAccount(@Param("account") String account);
	
}
