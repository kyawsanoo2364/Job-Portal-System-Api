package com.javadev.jobportal.controller;

import com.javadev.jobportal.dto.JobDTO;
import com.javadev.jobportal.dto.PageableDTO;
import com.javadev.jobportal.enums.JobType;
import com.javadev.jobportal.enums.SalaryPeriod;
import com.javadev.jobportal.response.ApiResponse;
import com.javadev.jobportal.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<?> getAllJobs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) boolean isNegotiable,
            @RequestParam(required = false) List<JobType> types,
            @RequestParam(required = false) List<SalaryPeriod> salaryPeriod,
            @PageableDefault Pageable pageable
    ) {
        PageableDTO<?> dto = jobService.getAll(search,title,location,minSalary,maxSalary,

                types,isNegotiable,salaryPeriod
                ,pageable);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        JobDTO jobDTO = jobService.getById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK,null,jobDTO)
        );
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobDTO jobDTO) {
        JobDTO jobDto = jobService.createJob(jobDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(HttpStatus.CREATED,"New job created successfully",jobDto)
        );
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobDTO jobDTO) {
        JobDTO jobDTOUpdated = jobService.updateJob(id, jobDTO);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK,"Job updated successfully",jobDTOUpdated)
        );
    }

    @PreAuthorize("hasRole('EMPLOYER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) throws IOException {
        jobService.deleteJob(id);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK,"Job deleted successfully",null)
        );
    }
}
