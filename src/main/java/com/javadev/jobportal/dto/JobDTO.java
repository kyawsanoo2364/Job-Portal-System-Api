package com.javadev.jobportal.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javadev.jobportal.entity.Job;
import com.javadev.jobportal.enums.JobType;
import com.javadev.jobportal.enums.SalaryPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDTO {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

    private boolean isNegotiable;

    @NotNull
    private SalaryPeriod salaryPeriod;

    @NotBlank
    private String location;

    @NotNull
    private Set<JobType> jobTypes;

    public JobDTO(Job entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.minSalary = entity.getMinSalary();
        this.maxSalary = entity.getMaxSalary();
        this.isNegotiable = entity.isNegotiable();
        this.salaryPeriod = entity.getSalaryPeriod();
        this.location = entity.getLocation();
        this.jobTypes = entity.getJobTypes();

    }
}
