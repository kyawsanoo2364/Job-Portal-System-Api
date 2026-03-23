package com.javadev.jobportal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javadev.jobportal.entity.JobApplication;
import com.javadev.jobportal.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobApplicationDTO {
    @NotBlank
    private String coverLetter;

    @NotBlank
    private Long fileId;

    private Long jobId;

}
