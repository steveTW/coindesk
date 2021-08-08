package com.test.coindesk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class CoindeskApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void getOriginalCoindesk() throws Exception{
		MvcResult result =mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/coindesk/origin")
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
	}

	@Test
	void getConvertedCoindesk() throws Exception {
		MvcResult result =mockMvc.perform(
						MockMvcRequestBuilders
								.get("/api/coindesk/converted")
								.contentType(MediaType.APPLICATION_JSON)
								.characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
	}

}
