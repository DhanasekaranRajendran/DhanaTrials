package com.target.myretail.productapi;

import com.target.myretail.productapi.domain.ProductPrice;
import com.target.myretail.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableCaching
@SpringBootApplication
public class ProductApiApplication {
    @Autowired
    private ProductRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
    }

    /**
     * Inserting sample data into the MongoDB database
     */
    @PostConstruct
    public void storeSampleData() {

        repository.deleteAll();
        ProductPrice product1 = new ProductPrice();
        product1.setId("13860428");
        product1.setProductName("The Big Lebowski (Blu-ray) (Widescreen)");
        ProductPrice.CurrentPrice currentPrice1 = product1.new CurrentPrice();
        currentPrice1.setValue(new BigDecimal(13.49).setScale(2, RoundingMode.HALF_EVEN));
        currentPrice1.setCurrencyCode("USD");
        product1.setCurrentPrice(currentPrice1);

        ProductPrice product2 = new ProductPrice();
        product2.setId("53536820");
        product2.setProductName("Apple iPad 9.7-inch Wi-Fi Only (2018 Model, 6th Generation)");
        ProductPrice.CurrentPrice currentPrice2 = product2.new CurrentPrice();
        currentPrice2.setValue(new BigDecimal(429.99).setScale(2, RoundingMode.HALF_EVEN));
        currentPrice2.setCurrencyCode("USD");
        product2.setCurrentPrice(currentPrice2);
        List<ProductPrice> productPriceList = Arrays.asList(product1, product2);
        repository.saveAll(productPriceList);
    }
}
