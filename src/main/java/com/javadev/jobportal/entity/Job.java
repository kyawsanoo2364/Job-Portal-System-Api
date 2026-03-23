package com.javadev.jobportal.entity;

import com.javadev.jobportal.enums.JobType;
import com.javadev.jobportal.enums.SalaryPeriod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Job extends BaseEntity {
    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false,columnDefinition = "TEXT")
    private String description;

    @Column(name = "posted_by",nullable = false)
    private UserInfo postedBy;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @Column(name = "salary_period",nullable = false)
    @Enumerated(EnumType.STRING)
    private SalaryPeriod salaryPeriod;

    @Column(name = "is_negotiable")
    @Builder.Default
    private boolean isNegotiable = false;

    @Column(name = "location")
    private String location;

    @Column(name = "types")
    @Enumerated(EnumType.STRING)
    private Set<JobType> jobTypes;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @Column(name = "applications")
    private List<JobApplication> applications;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Job job = (Job) o;
        return isNegotiable == job.isNegotiable && Objects.equals(title, job.title) && Objects.equals(description, job.description) && Objects.equals(postedBy, job.postedBy) && Objects.equals(minSalary, job.minSalary) && Objects.equals(maxSalary, job.maxSalary) && salaryPeriod == job.salaryPeriod && Objects.equals(location, job.location) && Objects.equals(jobTypes, job.jobTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, postedBy, minSalary, maxSalary, salaryPeriod, isNegotiable, location, jobTypes);
    }
}
