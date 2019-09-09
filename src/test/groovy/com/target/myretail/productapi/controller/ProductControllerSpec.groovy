package com.target.myretail.productapi.controller

import com.target.myretail.productapi.exception.GlobalProductExceptionHandler
import com.target.myretail.productapi.exception.PricingNotFoundException
import com.target.myretail.productapi.service.ProductService
import com.target.myretail.productapi.domain.ProductPrice
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import spock.lang.Specification;

@Unroll
class ProductControllerSpec extends Specification {
    ProductService mockProductService
    ProductController productController
    MockMvc mockMvc

    def setup() {
        mockProductService = Mock(ProductService)
        productController = new ProductController(mockProductService)
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalProductExceptionHandler())
                .build()
    }

    def "get All product info"() {
        given:
        def productList = [new ProductPrice(id: "1234", currentPrice: [value: 199.99, currencyCode: "USD"])]

        when:
        def response = this.mockMvc.perform(get("/myretail/products")).andExpect(status().isOk()).andReturn().getResponse()

        then:
        mockProductService.getAllProductPricing() >> productList
        print response.contentAsString
    }

    def "get product info for single id"() {
        when:
        def response = this.mockMvc.perform(get("/myretail/products/1234"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()

        then:
        mockProductService.getProductInfo(_) >> new ProductPrice(id: "1234", currentPrice: [value: 99.99, currencyCode: "USD"])
        def responseMap = new JsonSlurper().parseText(response.contentAsString)
        responseMap.id == "1234"
        responseMap.current_price.value == 99.99
    }

    def "get product info for invalid id"() {
        when:
        def response = this.mockMvc.perform(get("/myretail/products/1234"))
                .andReturn()
                .getResponse()

        then:
        mockProductService.getProductInfo(_) >> {
            throw new PricingNotFoundException("Product Info not available the requested Id")
        }
        response.status == HttpStatus.NOT_FOUND.value()
        assert new JsonSlurper().parseText(response.contentAsString).toString().contains("Product Info not available the requested Id")
    }

    def "get product name for an id"() {
        when:
        def response = this.mockMvc.perform(get("/myretail/productName/1234"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()

        then:
        mockProductService.getProductName(_) >> new ProductPrice(id: "1234", productName: 'Apple iPad 9.7-inch Wi-Fi Only', currentPrice: [value: 99.99, currencyCode: "USD"])
        assert new JsonSlurper().parseText(response.contentAsString).toString().contains("Apple iPad 9.7-inch Wi-Fi Only")
        // assert response.contentAsString.contains("Apple iPad 9.7-inch Wi-Fi Only")

    }

    def "get product name for an invalid id from RedSky"() {
        when:
        def response = this.mockMvc.perform(get("/myretail/productName/1234"))
                .andReturn()
                .getResponse()

        then:
        mockProductService.getProductName(_) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        response.status == HttpStatus.NOT_FOUND.value()

    }

    def "delete product info for an id"() {
        when:
        def response = this.mockMvc.perform(delete("/myretail/products/1234"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()

        then:
        //assert new JsonSlurper().parseText(response.contentAsString).toString().contains("Product record deleted successfully")
        assert response.contentAsString.contains("Product record deleted successfully")

    }

    def "Update Price for an id"() {
        when:
        def response = this.mockMvc.perform(put("/myretail/products/1234")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonOutput.toJson(new ProductPrice(id: "1234", currentPrice: [value: 99.99, currencyCode: "USD"]))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()

        then:
        mockProductService.upsertProductPricing(_) >> new ProductPrice(id: "1234", currentPrice: [value: 199.99, currencyCode: "USD"])
        def body = new JsonSlurper().parseText(response.contentAsString)
        assert body instanceof Map
        assert body.current_price.value == 199.99
    }


}
