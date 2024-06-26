package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDTO;
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		productDTO = new ProductDTO(product, product.getCategories());
		category = Factory.createCategory();

		// primeiro faça quando > ação (quando retornar)
		// simulação do comportamento findAll retornando uma página
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		// primeiro faça quando > ação (quando retornar)
		// simulação do service
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		// when primeiro faça quando > do ação (quando retornar)
		// simulação do findById (id existente e não existente)
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty())
				.thenThrow(ResourceNotFoundException.class);

		when(repository.getOne(existingId)).thenReturn(product);
		when(repository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		when(categoryRepository.getOne(existingId)).thenReturn(category);
		when(categoryRepository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		/// ação do > when quando (quando for void)
		doNothing().when(repository).deleteById(existingId);
		doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

	}

	@Test
	public void updateShouldReturnProductDTOWhenProductIdExists() {

		ProductDTO result = service.update(existingId, productDTO);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).getOne(existingId);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
		Mockito.verify(repository, Mockito.times(1)).getOne(nonExistingId);

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);

	}

	@Test
	public void findByIdShouldReturnProductDTOWhenProductIdExists() {
		productDTO = service.findById(existingId);
		Assertions.assertNotNull(productDTO);
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);

	}

	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}

	@Test
	public void deleteShoulThrowDataIntegrityViolationExceptionWhenIdIsDependent() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

		verify(repository, times(1)).deleteById(dependentId);
	}

	@Test
	public void deleteShoulThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		verify(repository, times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository, times(1)).deleteById(existingId);
	}

}
