package com.javadev.jobportal.dto;

import com.javadev.jobportal.entity.JobApplication;
import com.javadev.jobportal.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;

    private String coverLetter;

    private FileDTO file;

    private ApplicationStatus status;

    private UserInfoDTO candidate;

    public JobApplicationDTO(JobApplication entity){
        this.id = entity.getId();
        this.coverLetter = entity.getCoverLetter();
        this.file = new FileDTO(entity.getFile());
        this.status = entity.getStatus();
        this.candidate = new UserInfoDTO(entity.getCandidate());
    }
}
