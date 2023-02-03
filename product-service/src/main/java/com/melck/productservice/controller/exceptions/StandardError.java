package com.melck.productservice.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandardError {

    private Instant timeStamp;
    private Integer status;
    private String error;
    private String path;
}
