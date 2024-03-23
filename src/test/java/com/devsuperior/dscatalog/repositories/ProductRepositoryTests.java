package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	

	private long existingId;
	private long nonExistingId;
	private Product product;
	private long countTotalProducts;
	Optional<Product> result;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		product = Factory.createProduct();
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistsWhithAutoIncrementWhenIdIsNull() {
		
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1, product.getId());
	}

	@Test
	public void deleteShouldDeleteObjcetWhenIdExists() {

		repository.deleteById(existingId);
		result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());

	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {

			repository.deleteById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
		result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnOptionalEmptyWhenIdDoesNotExists() {
		result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}
	
	

}
