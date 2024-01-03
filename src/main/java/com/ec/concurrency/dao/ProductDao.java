package com.ec.concurrency.dao;

import com.ec.concurrency.entity.Product;

public interface ProductDao {
	public Product findById(long id);
}
