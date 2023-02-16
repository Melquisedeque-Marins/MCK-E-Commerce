package com.melck.reviewsservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {
    @NotNull(message = "The field userId is required")
    private Long userId;
    @NotBlank(message = "The field title is required")
    private String title;
    @NotBlank(message = "The field description is required")
    private String description;
    @PositiveOrZero(message = "The value of the rate field must be positive")
    @Min(value = 0)
    @Max(value = 5)
    private Integer rate;

}
