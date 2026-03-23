package com.javadev.jobportal.service;

import com.javadev.jobportal.dto.CreateJobApplicationDTO;
import com.javadev.jobportal.dto.JobApplicationDTO;
import com.javadev.jobportal.dto.PageableDTO;
import com.javadev.jobportal.dto.UpdateJobApplicationDTO;
import com.javadev.jobportal.entity.File;
import com.javadev.jobportal.entity.Job;
import com.javadev.jobportal.entity.JobApplication;
import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.enums.ApplicationStatus;
import com.javadev.jobportal.exceptions.CustomWebServiceException;
import com.javadev.jobportal.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;
    private final FileService fileService;
    private final UserService userService;
    private final MessageSource messageSource;

    @Transactional
    public JobApplicationDTO createJobApplication(CreateJobApplicationDTO jobApplicationDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }

        Job job = jobService.findById(jobApplicationDTO.getJobId());
        UserInfo candidate = userService.getUserByEmail(authentication.getName());
        File resumeFile = fileService.findById(jobApplicationDTO.getFileId());
        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .coverLetter(jobApplicationDTO.getCoverLetter())
                .file(resumeFile)
                .candidate(candidate)
                .status(ApplicationStatus.SUBMITTED)
                .build();
        JobApplication jobApplicationSaved = jobApplicationRepository.save(jobApplication);
        return new JobApplicationDTO(jobApplicationSaved);
    }

    public PageableDTO<?> getAll(Long jobId, Pageable pageable){
        Job job = jobService.findById(jobId);
        Page<JobApplication> page = jobApplicationRepository.findByJob(job,pageable);

        List<JobApplicationDTO> applicationDTOList = page.getContent().stream().map(JobApplicationDTO::new).toList();

        return new PageableDTO<>(applicationDTOList,page);
    }

    public JobApplicationDTO getById(Long id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage("jopApplication.not.found",null, LocaleContextHolder.getLocale())
                )
                );
        return new JobApplicationDTO(jobApplication);
    }

    public JobApplicationDTO update(Long id, UpdateJobApplicationDTO updateJobApplicationDTO){
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage("jopApplication.not.found",null, LocaleContextHolder.getLocale())
                )
        );
        jobApplication.setStatus(updateJobApplicationDTO.getStatus());
        JobApplication saved = jobApplicationRepository.save(jobApplication);

        return new  JobApplicationDTO(saved);
    }

    public void delete(Long id){
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage("jopApplication.not.found",null, LocaleContextHolder.getLocale())
                )
        );
        jobApplicationRepository.delete(jobApplication);
    }
}
