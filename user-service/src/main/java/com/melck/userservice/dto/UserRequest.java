package com.melck.userservice.dto;


import com.melck.userservice.enuns.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String fullName;
    private Gender gender;
    private LocalDate birthDate;
    private String cpf;
    private String phoneNumber;
    private Boolean whatsappNotifications;
    private String email;
    private Boolean emailNotifications;
    private String password;
    
}
