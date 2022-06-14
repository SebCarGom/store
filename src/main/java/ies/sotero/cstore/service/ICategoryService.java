package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import ies.sotero.cstore.model.Category;

public interface ICategoryService {
	public List<Category> findAll();

	public Category save(Category category);

	public Optional<Category> get(Integer id);

	public void update(Category category);

	public void delete(Integer id);
}
