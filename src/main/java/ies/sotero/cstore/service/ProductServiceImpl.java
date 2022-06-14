package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.sotero.cstore.model.Product;
import ies.sotero.cstore.repository.IProductRepository;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	@Override
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Optional<Product> get(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	public void update(Product product) {
		productRepository.save(product);
	}

	@Override
	public void delete(Integer id) {
		productRepository.deleteById(id);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public void applyDiscount(Double disc) {
		productRepository.priceDiscount(disc);
	}

	@Override
	public void undoDiscountApplied(Double disc) {
		productRepository.undoPriceDiscount(disc);
		
	}
}
