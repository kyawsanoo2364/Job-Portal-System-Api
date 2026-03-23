package com.javadev.jobportal.controller;

import com.javadev.jobportal.dto.CreateJobApplicationDTO;
import com.javadev.jobportal.dto.JobApplicationDTO;
import com.javadev.jobportal.dto.PageableDTO;
import com.javadev.jobportal.dto.UpdateJobApplicationDTO;
import com.javadev.jobportal.response.ApiResponse;
import com.javadev.jobportal.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job/applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/create")
    public ResponseEntity<?> createApplication(@RequestBody CreateJobApplicationDTO createJobApplicationDTO) {
        JobApplicationDTO dto = jobApplicationService.createJobApplication(createJobApplicationDTO);
        ApiResponse<?> res = new ApiResponse<>(
                HttpStatus.OK,
                messageSource.getMessage("jobApplication.created",null, LocaleContextHolder.getLocale()),
                dto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/all/{jobId}")
    public ResponseEntity<?> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable, @PathVariable Long jobId) {
        PageableDTO<?> pageableDTO = jobApplicationService.getAll(jobId, pageable);
        return ResponseEntity.ok(pageableDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        JobApplicationDTO dto = jobApplicationService.getById(id);
        ApiResponse<?> res = new ApiResponse<>(
                HttpStatus.OK,
                null,
                dto
        );
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable Long id, UpdateJobApplicationDTO requestDTO){
        JobApplicationDTO dto = jobApplicationService.update(id, requestDTO);
        ApiResponse<?> res = new ApiResponse<>(
                HttpStatus.OK,
                messageSource.getMessage("jobApplication.updated",null, LocaleContextHolder.getLocale()),
                dto
        );

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        jobApplicationService.delete(id);
        ApiResponse<?> res = new ApiResponse<>(
                HttpStatus.OK,
                messageSource.getMessage("jobApplication.deleted",null,LocaleContextHolder.getLocale())
        );
        return ResponseEntity.ok(res);
    }
}
