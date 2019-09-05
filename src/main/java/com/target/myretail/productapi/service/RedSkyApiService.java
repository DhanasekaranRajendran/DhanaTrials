package com.target.myretail.productapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RedSkyApiService {
    RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String redSkyUrl;
    private Environment environment;

    public RedSkyApiService(Environment environment, RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.environment = environment;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;

    }

    @PostConstruct
    public void init() {
        redSkyUrl = environment.getRequiredProperty("redsky.url");
    }

    public String getProductNameById(String productId) throws IOException {
        log.info("Calling RedSky API to get product name for ID: {}", productId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("productId", productId);
        String productString = restTemplate.getForObject(redSkyUrl, String.class, productId);
        log.info("redsky response: {}", productString);
        return extractProductName(productString);
    }

    public String extractProductName(String productData) throws IOException {
        JsonNode rootNode = objectMapper.readTree(productData);
        JsonNode productName = rootNode.get("product").get("item").get("product_description").get("title");
        return productName.asText();
    }
}
