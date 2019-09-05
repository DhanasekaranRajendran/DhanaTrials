package com.target.myretail.productapi.service;

import com.target.myretail.productapi.domain.ProductPrice;
import com.target.myretail.productapi.exception.InvalidInputException;
import com.target.myretail.productapi.exception.PricingNotFoundException;
import com.target.myretail.productapi.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedSkyApiService redSkyApiService;

    public ProductService(ProductRepository productRepository, RedSkyApiService redSkyApiService) {
        this.productRepository = productRepository;
        this.redSkyApiService = redSkyApiService;
    }

    public ProductPrice getProductInfo(String productId) throws IOException {
        log.info("Retrieving product info from DB for id: {}", productId);
        String productName = redSkyApiService.getProductNameById(productId);
        ProductPrice productPrice = productRepository.findById(productId)
                .orElseThrow(() -> new PricingNotFoundException("Pricing not available for this product"));
        productPrice.setProductName(productName);
        return productPrice;
    }

    public List<ProductPrice> getAllProductPricing() throws IOException {
        log.info("Retrieving all available product info from DB");
        List<ProductPrice> productPriceList = productRepository.findAll();
        for (ProductPrice productPrice : productPriceList) {
            productPrice.setProductName(redSkyApiService.getProductNameById(productPrice.getId()));
        }
        return productPriceList;
    }

    public ProductPrice getProductName(String productId) throws IOException {
        log.info("Retrieving product info from Redsky for id: {}", productId);
        String productName = redSkyApiService.getProductNameById(productId);
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProductName(productName);
        productPrice.setId(productId);
        return productPrice;
    }

    public void deleteProductById(String productId) {
        log.info("Deleting product info from DB for id: {}", productId);
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            log.info("Product not available in DB. : {}", productId);
            throw new PricingNotFoundException("Product info not available for the requested Product");
        }
    }

    public ProductPrice upsertProductPricing(ProductPrice productPrice) throws IOException {
        log.info("Updating product info in DB for product: {}", productPrice.toString());
        if (productRepository.existsById(productPrice.getId())) {
            return productRepository.save(productPrice);
        } else {
            try {
                //This call will confirm if the Product is available in RedSky Service
                redSkyApiService.getProductNameById(productPrice.getId());
                return productRepository.save(productPrice);
            } catch (HttpClientErrorException exception) {
                log.info("Pricing not available for product : {}", productPrice.getId());
                throw exception;
            }
        }
    }

}
