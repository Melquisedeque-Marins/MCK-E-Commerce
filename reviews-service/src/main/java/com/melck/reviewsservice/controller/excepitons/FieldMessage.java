package com.melck.reviewsservice.controller.excepitons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldMessage {
        private String fieldName;
        private String message;

}
