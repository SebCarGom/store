package ies.sotero.cstore.service;

import java.util.List;
import java.util.Optional;

import ies.sotero.cstore.model.Order;
import ies.sotero.cstore.model.CustomUser;

public interface IOrderService {
	public List<Order> findAll();

	public Order save(Order order);

	public String generateOrderNumber();

	public List<Order> findByUser(CustomUser user);

	public Optional<Order> findById(Integer id);
}
