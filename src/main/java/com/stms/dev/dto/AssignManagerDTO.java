package com.stms.dev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignManagerDTO {
    private long projectId;
    private long managerId;
}
