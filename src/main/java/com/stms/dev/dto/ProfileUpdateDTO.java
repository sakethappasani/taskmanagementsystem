package com.stms.dev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}

