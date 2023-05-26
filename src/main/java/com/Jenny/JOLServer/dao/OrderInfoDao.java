package com.Jenny.JOLServer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Jenny.JOLServer.model.Order;

public interface OrderInfoDao extends JpaRepository<Order, Integer> {
	List<Order> findByAccount(String account);
}
