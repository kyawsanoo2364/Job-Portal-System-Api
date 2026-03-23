package com.javadev.jobportal.entity;

import com.javadev.jobportal.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_applications")
public class JobApplication extends  BaseEntity {
    @Column(name = "cover_letter",nullable = false,columnDefinition = "TEXT")
    private String coverLetter;

    @ManyToOne
    @JoinColumn(name = "file")
    private File file;

    @ManyToOne
    @JoinColumn(name = "candidate")
    private UserInfo candidate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status",nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @ManyToOne
    @JoinColumn(name = "job")
    private Job job;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JobApplication that = (JobApplication) o;
        return Objects.equals(coverLetter, that.coverLetter) && Objects.equals(file, that.file) && Objects.equals(candidate, that.candidate) && status == that.status && Objects.equals(job, that.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coverLetter, file, candidate, status, job);
    }
}
