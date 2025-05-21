package com.stms.dev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
}
