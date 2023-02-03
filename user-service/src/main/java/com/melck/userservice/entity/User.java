package com.melck.userservice.entity;

import com.melck.userservice.enuns.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private Boolean whatsappNotifications;
    private String email;
    private Boolean emailNotifications;
    private String password;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;


    @PrePersist
    public void preCreated(){
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preupdated(){
        updatedAt = Instant.now();
    }


}
