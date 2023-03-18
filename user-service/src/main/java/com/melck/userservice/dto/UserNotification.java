package com.melck.userservice.dto;

import com.melck.userservice.enuns.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotification implements Serializable {
    private String fullName;
    private String email;
}
