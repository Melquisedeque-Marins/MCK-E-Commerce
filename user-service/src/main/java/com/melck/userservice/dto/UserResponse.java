package com.melck.userservice.dto;

import com.melck.userservice.enuns.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private Gender gender;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private Boolean whatsappNotifications;
    private String email;
    private Boolean emailNotifications;
    private String password;
    private Long cartId;
}
