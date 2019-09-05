package com.target.myretail.productapi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Document(collection = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductPrice {


    @Id
    @NotNull(message="Product Id cannot be null")
    private String id;

    @Transient
    private String productName;

    @JsonProperty("current_price")
    @Field("current_price")
    @Valid
    private CurrentPrice currentPrice;

    @Data
    public
    class CurrentPrice {

        @Positive(message = "Price should not be negative")
        @NotNull(message = "Price cannot be null")
        private BigDecimal value;

        @JsonProperty("currency_code")
        @Field("currency_code")
        @NotBlank(message="Currency code should not be null")
        private String currencyCode;
    }
}
