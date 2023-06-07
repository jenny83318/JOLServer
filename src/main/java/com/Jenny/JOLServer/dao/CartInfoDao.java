package com.Jenny.JOLServer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Jenny.JOLServer.model.Cart;

@Repository
public interface CartInfoDao extends JpaRepository<Cart, Integer> {
	List<Cart> findByAccountOrderByUpdateDtDesc(String account);
	List<Cart> findByAccountAndIsCartOrderByUpdateDtDesc(String account, boolean isCart);
	List<Cart> findByAccountAndIsCartAndProdId(String account, boolean isCart, int prodId);
	List<Cart> findByAccountAndIsCartAndProdIdAndSize(String account, boolean isCart,int cartId, String size);
}
