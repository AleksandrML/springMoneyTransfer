package com.example.springmoneytransfer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests {
	@Autowired
	TestRestTemplate restTemplate;

	private static final GenericContainer<?> container = new GenericContainer<>("money-transfer:latest")
			.withExposedPorts(5500);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeAll
	public static void setUp() {
		container.start();
	}

	@Test
	void testTransfer() throws JsonProcessingException {
		//given
		String requestJson = "{\"cardFromNumber\": \"not_valid\", \"cardFromValidTill\": \"12/25\"," +
				" \"cardFromCVV\": \"330\", \"cardToNumber\": \"not_valid\"," +
				"\"amount\": {\"value\": 1000, \"currency\": \"RUR\"}}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

		//expected
		String expectedMessage = "\"Acceptor card is not acceptable for transaction\"";

		//when
		ResponseEntity<String> response =
				restTemplate.postForEntity("http://localhost:" + container.getMappedPort(5500) + "/transfer", entity, String.class);
		JsonNode root = objectMapper.readTree(response.getBody());
		String realMessage = root.path("message").toString();

		//then
		Assertions.assertEquals(expectedMessage, realMessage);
	}

}
