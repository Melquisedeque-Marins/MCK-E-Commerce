package com.melck.cartservice.entity;

import jakarta.persistence.Column;

import java.util.List;

public class Product {

    private Long id;
    private String name;
    private String skuCode;
    private String description;
    private double rate;
    private double price;
    private Integer quantity;
    private List<String> imgUrl;
}
