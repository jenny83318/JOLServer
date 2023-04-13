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

@RestController
@RequestMapping("/index")
public class RestfulController {
	
	@Autowired()
	private CustomerInfoDao custDao;

	@GetMapping("/")
	public List<Customer> getAllCustData() {
		return custDao.findAll();
	}

	@PostMapping("/insert")
	public Customer addCustomer(@RequestBody Customer customer) {
		return custDao.save(customer);
	}

	@GetMapping("/{account}")
	public Customer getCustomerById(@PathVariable String account) throws Exception {
		return custDao.findByAccount(account);
	}

	@PutMapping("/update/{account}")
	public Customer updateProduct(@PathVariable String account, @RequestBody Customer c) throws Exception {
		Customer cust = custDao.findByAccount(account);
		Customer updCust = Customer.builder().account(cust.getAccount()).address(c.getAddress()).email(c.getEmail()).name(c.getName()).password(c.getPassword()).phone(c.getPhone()).status(c.getStatus()).build();
		return custDao.save(updCust);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteProduct(@PathVariable String id) {
		int cusId = Integer.parseInt(id);
		custDao.deleteById(cusId);
	}

}
