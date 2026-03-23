package com.javadev.jobportal.repository;

import com.javadev.jobportal.entity.Job;
import com.javadev.jobportal.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    Page<JobApplication> findByJob(Job job, Pageable pageable);
}
