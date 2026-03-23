package com.javadev.jobportal.repository;

import com.javadev.jobportal.entity.File;
import com.javadev.jobportal.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {
    Page<File> findByUser(UserInfo user, Pageable pageable);
}
