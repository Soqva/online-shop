package com.s0qva.application.dto.product.detail;

import com.s0qva.application.dto.ReadingDto;
import com.s0qva.application.model.enumeration.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailsReadingDto implements ReadingDto {
    private Long id;
    private String description;
    private Country madeIn;

    @Override
    public String toString() {
        return description + " " + madeIn + "\n";
    }
}
