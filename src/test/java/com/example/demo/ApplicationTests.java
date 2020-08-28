package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {
	private static final ObjectMapper om = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;

	@Test
	void integrationTest() throws Exception {
		mockMvc.perform(get("/settlements/tickets")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());

		mockMvc.perform(post("/settlements/tickets/create")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("tickets", "20"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());

		mockMvc.perform(get("/settlements/tickets")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());

		LocalDate now = LocalDate.now();
		mockMvc.perform(get("/settlements/tickets/"+now.format(DateTimeFormatter.ISO_LOCAL_DATE))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());

		LocalDate date = now.minusYears(1);
		mockMvc.perform(get("/settlements/tickets/"+date.format(DateTimeFormatter.ISO_LOCAL_DATE))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}
}
