package com.stms.dev.dto;

import com.stms.dev.models.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String name;
    private String username;
    private String email;
    private Role role;
}
