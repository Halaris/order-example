package org.nordnet.orderexample.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nordnet.orderexample.OrderExampleApplication;
import org.nordnet.orderexample.configuration.Config;
import org.nordnet.orderexample.model.OrdersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;


import java.math.BigDecimal;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrderControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("OrderController"));
    }

    @Test
    void getOrder_whenOrderNotFound() throws Exception{
        this.mockMvc.perform(get("/order/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        ;
    }

    @Test
    void addOrder() throws Exception {
        OrdersDto order = OrdersDto.builder()
                .action("BUY")
                .ticker("AMAZON")
                .price(BigDecimal.valueOf(1.32))
                .currency("USD")
                .volume(100)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(order );

        MvcResult result = this.mockMvc.perform(post("/order")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON_UTF8)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        OrdersDto resultDto = mapper.createParser(result.getResponse().getContentAsString()).readValueAs(OrdersDto.class);

        this.mockMvc.perform(get("/order/" + resultDto.getId()))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    void getSummary() throws Exception {
        BigDecimal min = BigDecimal.valueOf(1000);
        BigDecimal max = BigDecimal.ZERO;
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        for (int i = 0; i < 10; i++) {
            OrdersDto order = createParametizedOrder("Test", "BUY", "SEK");
            String requestJson = ow.writeValueAsString(order);
            min = min.min(order.getPrice());
            max = max.max(order.getPrice());
            this.mockMvc.perform(post("/order")
                            .content(requestJson)
                            .contentType(APPLICATION_JSON_UTF8)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
        min = min.round(new MathContext(3, RoundingMode.HALF_UP));
        max = max.round(new MathContext(3, RoundingMode.HALF_UP));
        Thread.sleep(1000);
        this.mockMvc.perform(post("/test")).andExpect(MockMvcResultMatchers.status().isOk());
        Thread.sleep(1000);
        this.mockMvc.perform(get("/order/ticker/TEST/action/BUY/currency/SEK"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").value(min))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").value(max));

    }

    private OrdersDto createParametizedOrder(String ticker, String action, String Currency) {
        return OrdersDto.builder()
                .action(action)
                .ticker(ticker)
                .price(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1,3)))
                .currency(Currency)
                .volume(ThreadLocalRandom.current().nextInt(50,200))
                .build();
    }
}