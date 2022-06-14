package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import ies.sotero.cstore.model.Product;

public interface IProductService {
	public Product save(Product product);

	public Optional<Product> get(Integer id);

	public void update(Product product);

	public void delete(Integer id);

	public List<Product> findAll();

	public void applyDiscount(Double disc);
	
	public void undoDiscountApplied(Double disc);
}
