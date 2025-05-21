package com.stms.dev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManageRoleDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String role;
}
