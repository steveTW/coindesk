package com.test.coindesk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.coindesk.currencymapping.CurrencyNameMapping;
import com.test.coindesk.repository.CurrencyNameMappingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class CurrencyNameMappingApiTest {
    @Autowired
    private MockMvc mockMvc;

    private CurrencyNameMapping record1 = new CurrencyNameMapping("USD", "美元");


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CurrencyNameMappingRepository repository;

    @Test
    public void testGet() throws Exception {

        Mockito.when(repository.findById(record1.getCurrency())).thenReturn(Optional.of(record1));

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/currency/mapping/{0}", record1.getCurrency())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(mapper.writeValueAsString(record1));
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetNotExists() throws Exception {
        Mockito.when(repository.findById(record1.getCurrency())).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/currency/mapping/{0}", record1.getCurrency())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("查無資料"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void testInsert() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("JPY", "日元");

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/currency/mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isOk())
                .andExpect(content().string("JPY"))
                .andReturn();
    }

    @Test
    public void testInsertFailed() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("JPY", "日元");

        Mockito.doThrow(new RuntimeException("test")).when(repository).save(testMapping);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/currency/mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("新增失敗"))
                .andReturn();
    }

    @Test
    public void testInsertAssert() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("", "日元");


        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/currency/mapping")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("新增失敗"))
                .andReturn();
    }

    @Test
    public void testUpdate() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("USD", "美金");

        Mockito.when(repository.findById(testMapping.getCurrency())).thenReturn(Optional.of(record1));

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/currency/mapping/{0}", testMapping.getCurrency())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8)).isEqualTo(mapper.writeValueAsString(testMapping));
        System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void testUpdateFailed() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("USD", "美金");
        Mockito.when(repository.findById(testMapping.getCurrency())).thenReturn(Optional.of(record1));
        Mockito.doThrow(new RuntimeException("test")).when(repository).save(testMapping);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/currency/mapping/{0}", testMapping.getCurrency())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("更新失敗"))
                .andReturn();
    }

    @Test
    public void testUpdateNotExists() throws Exception {
        CurrencyNameMapping testMapping =
                new CurrencyNameMapping("JPY", "日元");
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/currency/mapping/{0}", testMapping.getCurrency())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(mapper.writeValueAsString(testMapping)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("查無資料"))
                .andReturn();
    }

    @Test
    public void testDelete() throws Exception {
        String testKey = "USD";
        Mockito.when(repository.findById(testKey)).thenReturn(Optional.of(record1));

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/currency/mapping/{0}", "USD")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testDeleteNotExists() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/currency/mapping/{0}", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("查無資料"))
                .andReturn();
    }

    @Test
    public void testDeleteException() throws Exception {
        String testKey = "USD";
        Mockito.when(repository.findById(testKey)).thenReturn(Optional.of(new CurrencyNameMapping()));
        Mockito.doThrow(new RuntimeException("test")).when(repository).deleteById(testKey);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/currency/mapping/{0}", testKey)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("刪除失敗"))
                .andReturn();


    }
}
