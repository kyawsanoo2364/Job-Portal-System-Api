package com.javadev.jobportal.service;

import com.javadev.jobportal.dto.JobDTO;
import com.javadev.jobportal.dto.PageableDTO;
import com.javadev.jobportal.entity.File;
import com.javadev.jobportal.entity.Job;
import com.javadev.jobportal.entity.JobApplication;
import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.enums.JobType;
import com.javadev.jobportal.enums.SalaryPeriod;
import com.javadev.jobportal.exceptions.CustomWebServiceException;
import com.javadev.jobportal.repository.JobRepository;
import com.javadev.jobportal.specifications.JobSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final UserService userService;
    private final MessageSource messageSource;
    private final FileService fileService;

    @Transactional
    public JobDTO createJob(JobDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }
        UserInfo user = userService.getUserByEmail(authentication.getName());
        Job newJob = Job.builder()
                .title(request.getTitle())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .isNegotiable(request.isNegotiable())
                .description(request.getDescription())
                .jobTypes(request.getJobTypes())
                .salaryPeriod(request.getSalaryPeriod())
                .location(request.getLocation())
                .postedBy(user)
                .build();
        Job savedJob = jobRepository.save(newJob);
        return new JobDTO(savedJob);
    }

    public PageableDTO<JobDTO> getAll(
                                      String search,
                                      String title,
                                      String location,
                                      BigDecimal minSalary,
                                      BigDecimal maxSalary,

                                      List<JobType> types,
                                      boolean isNegotiable,
                                      List<SalaryPeriod> salaryPeriods
            ,Pageable pageable
    ) {
        Specification<Job> spec = JobSpec.getAllJobs(search, title, minSalary, maxSalary, location, types, isNegotiable, salaryPeriods);
        Page<Job> page = jobRepository.findAll(spec,pageable);
        List<JobDTO> jobList = page.getContent().stream().map(JobDTO::new).toList();
        return new PageableDTO<>(jobList,page);
    }

    public Job findById(Long id){
        return jobRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(HttpStatus.NOT_FOUND,
                        messageSource.getMessage("job.notfound",null, LocaleContextHolder.getLocale())
                )
        );
    }

    public  JobDTO getById(long id) {
        Job job = findById(id);
        return new JobDTO(job);
    }

    public JobDTO updateJob(long id, JobDTO request) {
        Job job = jobRepository.findById(id).orElseThrow(()->
             CustomWebServiceException.of(HttpStatus.NOT_FOUND,
                    messageSource.getMessage("job.notfound",null, LocaleContextHolder.getLocale())
            )
        );
        checkPermission(job);
        job.setTitle(request.getTitle());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setNegotiable(request.isNegotiable());
        job.setDescription(request.getDescription());
        job.setJobTypes(request.getJobTypes());
        job.setSalaryPeriod(request.getSalaryPeriod());
        job.setLocation(request.getLocation());

        Job savedJob = jobRepository.save(job);

        return new JobDTO(savedJob);
    }

    @Transactional
    public void deleteJob(long id) throws IOException {
        Job job = jobRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(HttpStatus.NOT_FOUND,
                        messageSource.getMessage("job.notfound",null, LocaleContextHolder.getLocale())
                )
        );
        checkPermission(job);
        for(JobApplication app: job.getApplications()){
            fileService.deleteFile(app.getFile().getId(),true);
        }
        job.getApplications().clear();
        jobRepository.delete(job);
    }

    private void checkPermission(Job job) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        };
        UserInfo user = userService.getUserByEmail(authentication.getName());
        if(!job.getPostedBy().equals(user)) {
            throw CustomWebServiceException.of(
                    HttpStatus.FORBIDDEN,
                    messageSource.getMessage("job.forbidden",null, LocaleContextHolder.getLocale())
            );
        }
    }
}
