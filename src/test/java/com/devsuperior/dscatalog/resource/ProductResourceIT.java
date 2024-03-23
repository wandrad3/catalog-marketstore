package com.devsuperior.dscatalog.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

//teste de integração na camada web. Carrega todo o contexto da aplicação até o banco de dados.
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private long countTotalProducts;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDTO;
	private Category category;
	private String jsonBody;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		productDTO = Factory.createProductDTO();
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		countTotalProducts = 25l;
		jsonBody = objectMapper.writeValueAsString(productDTO);

	}

	// page=0&size=10&sort=name,desc

	@Test
	public void findAllShouldReurnSortedPageWhenSortByName() throws Exception {
		ResultActions result = mockMvc
				.perform(get("/products?page=0&size=12&sort=name,asc").accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

		ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
	}

	@Test
	public void updateShouldRetunrResourceNotFoundWhenIdDoesNotExists() throws Exception {

		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

}
