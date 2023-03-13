package com.melck.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private Boolean whatsappNotifications;
    private String email;
    private Boolean emailNotifications;
    private String password;
    private Long cartId;
}
