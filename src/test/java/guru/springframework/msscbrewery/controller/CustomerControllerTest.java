package guru.springframework.msscbrewery.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.msscbrewery.service.CustomerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.CustomerDto;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;


import java.util.UUID;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	CustomerService customerService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	static CustomerDto validCustomer;
	
	@BeforeAll
	public static void setup() {
		validCustomer= CustomerDto.builder()
				.id(UUID.randomUUID())
				.name("John Smith")
				.build();
	}

	@Test
	void handleGet() throws Exception {
		given(customerService.getCustomerById(any(UUID.class))).willReturn(validCustomer);
		
		mockMvc
			.perform(
				get("/api/v1/customers/" + validCustomer.getId())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(validCustomer.getId().toString())))
			.andExpect(jsonPath("$.name", is("John Smith")));
			
	}
	
	@Test
	void handlePost() throws Exception {
		//given
		CustomerDto customerDto= validCustomer;
		String customeDtoJson= objectMapper.writeValueAsString(customerDto);
		
		CustomerDto savedCustomerDto= CustomerDto.builder().id(UUID.randomUUID()).name("new John").build();
		
		given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(savedCustomerDto);
		
		mockMvc.perform(
				post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(customeDtoJson))
			.andExpect(status().isCreated());
	}
	
	@Test
	void handlePut() throws Exception {
		CustomerDto updatedCustomerDto= validCustomer;
		updatedCustomerDto.setName("updated John");
		String updatedCustomerJson= objectMapper.writeValueAsString(updatedCustomerDto);
		//given(customerService.updateCustomer(any()))
		
		mockMvc.perform(
				put("/api/v1/customers/" + validCustomer.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatedCustomerJson))
		.andExpect(status().isNoContent());
	}
	
	@Test
	void handleDelete() throws Exception {
		
		mockMvc.perform(
				delete("/api/v1/customers/" + validCustomer.getId()))
		.andExpect(status().isNoContent());
	}

}
