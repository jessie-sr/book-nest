package com.pro.mybooklist.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartBookRepository extends CrudRepository<CartBook, Long>{
	Optional<CartBook> findById(CartBookKey backetBookId);
	
	List<CartBook> findByBacket(Cart cart);
	
	long deleteByBacket(Cart cart);
	
	long deleteById(CartBookKey backetBookId);
}
