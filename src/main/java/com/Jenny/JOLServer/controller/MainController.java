package com.Jenny.JOLServer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Jenny.JOLServer.dao.CustomerInfoDao;
import com.Jenny.JOLServer.model.Customer;
import com.Jenny.JOLServer.service.JOLCustomerInfo;

@RestController
@RequestMapping("/index")
public class MainController {

	@Autowired()
	private JOLCustomerInfo custService;
	
	@Autowired()
	private CustomerInfoDao custDao;

	@GetMapping("/")
	public List<Customer> getAllCustData() {
		return custService.getAllCustomerInfo();
	}

	@PostMapping("/insert")
	public Customer createProduct(@RequestBody Customer product) {
		return custDao.save(product);
	}

	@GetMapping("/{id}")
	public Customer getProductById(@PathVariable Long id) throws Exception {
		return custDao.findById(id).orElseThrow(() -> new Exception("Product not found"));
	}

	@PutMapping("/update/{id}")
	public Customer updateProduct(@PathVariable Long id, @RequestBody Customer c) throws Exception {
		Customer cust = custDao.findById(id).orElseThrow(() ->  new Exception("Product not found"));
		Customer updCust = Customer.builder().account(c.getAccount()).address(c.getAddress()).email(c.getEmail()).idno(cust.getIdno()).custId(cust.getCustId()).name(cust.getName()).password(c.getPassword()).phone(c.getPhone()).status(cust.getStatus()).build();
		return custDao.save(updCust);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteProduct(@PathVariable Long id) {
		custDao.deleteById(id);
	}

}
