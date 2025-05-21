package com.stms.dev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignDevelopersDTO {
    @NotNull
    private Long projectId;
    @NotNull
    private List<Long> developerIds;
}
