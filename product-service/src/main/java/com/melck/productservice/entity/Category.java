package com.melck.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

//    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
//    private Instant createdAt;
//
//    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
//    private Instant updatedAt;
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = Instant.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        updatedAt = Instant.now();
//    }
}
