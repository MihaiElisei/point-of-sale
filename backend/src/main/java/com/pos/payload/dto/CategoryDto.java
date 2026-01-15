package com.pos.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private Long storeId;
    private String name;
}
