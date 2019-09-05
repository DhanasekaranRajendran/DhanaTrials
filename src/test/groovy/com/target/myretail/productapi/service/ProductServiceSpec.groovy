package com.target.myretail.productapi.service

import com.target.myretail.productapi.domain.ProductPrice
import com.target.myretail.productapi.exception.PricingNotFoundException
import com.target.myretail.productapi.repository.ProductRepository
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

class ProductServiceSpec extends Specification {
    ProductService mockProductService
    ProductRepository mockRepository
    RedSkyApiService mockRedSkyApiService

    def setup() {
        mockRepository = Mock(ProductRepository)
        mockRedSkyApiService = Mock(RedSkyApiService)
        mockProductService = new ProductService(mockRepository, mockRedSkyApiService)
    }

    def "get product info for a valid item"() {
        when:
        def result = mockProductService.getProductInfo("12345")

        then:
        1 * mockRedSkyApiService.getProductNameById("12345") >> "Test Product Name"
        1 * mockRepository.findById(_) >> Optional.of(new ProductPrice())
        assert result instanceof ProductPrice
        assert result.productName == "Test Product Name"

    }

    def "get product info for an invalid item"() {
        when:
        def result = mockProductService.getProductInfo("12345")

        then:
        1 * mockRedSkyApiService.getProductNameById("12345") >> "Test Product Name"
        1 * mockRepository.findById(_) >> Optional.empty()
        thrown(PricingNotFoundException)

    }

    def "delete product info for an item not present in DB"() {
        when:
        mockProductService.deleteProductById("12345")

        then:
        1 * mockRepository.existsById(_) >> false
        0 * mockRepository.deleteById(_)
        thrown(PricingNotFoundException)
    }

    def "delete product info for an item present in DB"() {
        when:
        mockProductService.deleteProductById("12345")

        then:
        1 * mockRepository.existsById(_) >> true
        1 * mockRepository.deleteById(_)
    }

    def "update product price for an item present in DB"() {
        when:
        mockProductService.upsertProductPricing(new ProductPrice())

        then:
        1 * mockRepository.existsById(_) >> true
        1 * mockRepository.save(_)
        0 * mockRedSkyApiService.getProductNameById(_)
    }

    def "update product price for an item not present in DB"() {
        when:
        mockProductService.upsertProductPricing(new ProductPrice())

        then:
        1 * mockRepository.existsById(_) >> false
        1 * mockRepository.save(_)
        1 * mockRedSkyApiService.getProductNameById(_) >> "Test Name"
    }

    def "update product price for an item not present in DB and not present in redSky"() {
        when:
        mockProductService.upsertProductPricing(new ProductPrice())

        then:
        1 * mockRepository.existsById(_) >> false
        1 * mockRedSkyApiService.getProductNameById(_) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        thrown(HttpClientErrorException)
    }
}
