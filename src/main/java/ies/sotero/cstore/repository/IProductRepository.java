package ies.sotero.cstore.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.sotero.cstore.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

	@Transactional
	@Modifying
	@Query(value = "update products p set p.price = p.price*:discount", nativeQuery = true)
	public void priceDiscount(@Param(value = "discount") double disc);
	
	@Transactional
	@Modifying
	@Query(value = "update products p set p.price = p.price/:undoDiscount", nativeQuery = true)
	public void undoPriceDiscount(@Param(value = "undoDiscount") double disc);

}
