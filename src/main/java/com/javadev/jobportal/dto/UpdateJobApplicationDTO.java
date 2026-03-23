package com.javadev.jobportal.dto;

import com.javadev.jobportal.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobApplicationDTO {
    @NotNull
    private ApplicationStatus status;
}
