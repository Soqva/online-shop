package com.s0qva.application.mapper.product.detail;

import com.s0qva.application.dto.product.detail.ProductDetailsReadingDto;
import com.s0qva.application.mapper.Mapper;
import com.s0qva.application.model.ProductDetails;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailsToReadingMapper implements Mapper<ProductDetails, ProductDetailsReadingDto> {

    @Override
    public ProductDetailsReadingDto map(ProductDetails productDetails) {
        if (productDetails != null) {
            return ProductDetailsReadingDto.builder()
                            .id(productDetails.getId())
                            .description(productDetails.getDescription())
                            .madeIn(productDetails.getMadeIn())
                            .build();
        }

        return new ProductDetailsReadingDto();
    }
}
