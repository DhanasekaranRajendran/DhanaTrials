package com.target.myretail.productapi.controller;

import com.target.myretail.productapi.domain.ProductPrice;
import com.target.myretail.productapi.exception.InvalidInputException;
import com.target.myretail.productapi.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.target.myretail.productapi.common.ProductControllerConstants.*;

@Slf4j
@Api(value = "MyRetailProductAPI")
@RequestMapping("/myretail")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Gets productId name from Redsky service and pricing information from MongoDB NoSQL
     * database and gives out a JSON response.
     *
     * @param id
     * @return A list of products or single product with name and price
     * @throws IOException
     */
    @ApiOperation(GET_PRODUCT_INFO_BY_ID)
    @GetMapping(value = {"/products", "/products/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductInfo(@PathVariable("id") Optional<String> id) throws IOException {
        if (id.isPresent()) {
            log.info("Retrieving product information for id:{}", id);
            ProductPrice productPrice = productService.getProductInfo(id.get());
            return ResponseEntity.ok(productPrice);
        } else {
            log.info("Retrieving product information for all available products");
            List<ProductPrice> productPriceList = productService.getAllProductPricing();
            return ResponseEntity.ok(productPriceList);
        }
    }

    /**
     * Gets productId name from Redsky api service by product_id.
     *
     * @param productId
     * @return Product with name
     * @throws IOException
     */
    @ApiOperation(GET_PRODUCT_NAME_BY_ID)
    @GetMapping(value = {"/productName/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductName(@PathVariable("id") String productId) throws IOException {
        log.info("Retrieving product name for id:{}", productId);
        ProductPrice productPrice = productService.getProductName(productId);
        return ResponseEntity.ok(productPrice);
    }

    /**
     * Deletes product info by product_id in mongoDb.
     *
     * @param productId
     * @return Deletion status message
     */
    @ApiOperation(DELETE_PRODUCT_INFO_ID)
    @DeleteMapping(value = {"/products/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteProductById(@PathVariable("id") String productId) {
        log.info("Deleting product info for id:{}", productId);
        productService.deleteProductById(productId);
        return ResponseEntity.ok("Product record deleted successfully");
    }

    /**
     * creates or update product info by product_id.
     *
     * @param productId
     * @param productPrice
     * @return Product with name
     * @throws IOException
     */
    @ApiOperation(UPDATE_PRODUCT_INFO_ID)
    @PutMapping(value = {"/products/{id}"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductPrice> updateProductInfo(@PathVariable("id") String productId, @RequestBody @Valid ProductPrice productPrice) throws IOException {
        if (!productId.equalsIgnoreCase(productPrice.getId())) {
            log.info(INVALID_INPUT_FOR_UPDATE);
            throw new InvalidInputException(INVALID_INPUT_FOR_UPDATE);
        }
        ProductPrice updatedProductPrice = productService.upsertProductPricing(productPrice);
        return ResponseEntity.ok(updatedProductPrice);
    }

}
