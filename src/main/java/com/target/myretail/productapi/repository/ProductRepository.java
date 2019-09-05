package com.target.myretail.productapi.repository;

import com.target.myretail.productapi.domain.ProductPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductPrice, String> {
}
