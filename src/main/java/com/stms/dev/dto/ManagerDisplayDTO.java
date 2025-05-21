package com.stms.dev.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerDisplayDTO {
    private long managerId;
    private String managerName;
}
