package com.melck.productservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private Long userId;
    private Long productId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate createdAt;
    private Integer rate;
}
