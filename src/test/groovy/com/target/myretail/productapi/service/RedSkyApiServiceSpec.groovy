package com.target.myretail.productapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.env.Environment
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RedSkyApiServiceSpec extends Specification {
    RedSkyApiService mockRedSkyApiService
    RestTemplate mockRestTemplate
    ObjectMapper mockObjectMapper
    Environment mockEnvironment
    RestTemplateBuilder mockRestTemplateBuilder

    def setup() {
        mockRestTemplateBuilder = Mock(RestTemplateBuilder)
        mockRestTemplate = Mock(RestTemplate)
        mockRestTemplateBuilder.build() >> mockRestTemplate
        mockObjectMapper = Mock(ObjectMapper)
        mockEnvironment = Mock(Environment)
        mockRedSkyApiService = new RedSkyApiService(mockEnvironment, mockRestTemplateBuilder, mockObjectMapper)
        mockEnvironment.getRequiredProperty(_) >> "someURL"
    }

    def "get product name from redSky service"() {
        given:
        String redSkyResponse = "{\"product\":{\"item\":{\"tcin\":\"53536820\",\"product_description\":{\"title\":\"Apple iPad\"}}}}"

        when:
        String productName = mockRedSkyApiService.getProductNameById("12345")

        then:
        mockRestTemplate.getForObject(_, String.class, "12345") >> redSkyResponse
        mockObjectMapper.readTree(_) >> new ObjectMapper().readTree(redSkyResponse)
        productName == 'Apple iPad'
    }
}
