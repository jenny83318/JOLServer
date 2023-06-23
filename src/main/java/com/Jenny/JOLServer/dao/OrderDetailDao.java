package com.Jenny.JOLServer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jenny.JOLServer.model.OrderDetail;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail, Integer>{
	List<OrderDetail> findByOrderNoAndAccountOrderByProdId(int orderNo, String account);
	
}
