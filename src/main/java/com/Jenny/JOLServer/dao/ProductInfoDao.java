package com.Jenny.JOLServer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jenny.JOLServer.model.Product;

@Repository
public interface ProductInfoDao extends JpaRepository<Product, Integer> {
	Product findByProdId(int id);
	List<Product> findByNameContaining(String keyword);

}
