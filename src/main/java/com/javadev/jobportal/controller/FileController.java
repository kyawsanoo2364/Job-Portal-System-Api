package com.javadev.jobportal.controller;

import com.javadev.jobportal.dto.FileDTO;
import com.javadev.jobportal.dto.UpsertFileDTO;
import com.javadev.jobportal.response.ApiResponse;
import com.javadev.jobportal.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resumes")
public class FileController {
    private final FileService fileService;
    private final MessageSource messageSource;

    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/file/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        FileDTO fileDTO = fileService.uploadFile(file);
        ApiResponse<?> apiResponse = new ApiResponse<>(
                HttpStatus.CREATED,
                messageSource.getMessage("file.created",null,LocaleContextHolder.getLocale()),
                fileDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(fileService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        FileDTO file = fileService.getById(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(
                HttpStatus.OK,
                null,
                file
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody UpsertFileDTO fileDTO) {
        FileDTO file = fileService.updateFile(id,fileDTO.getFilename());
        ApiResponse<?> apiResponse = new ApiResponse<>(
                HttpStatus.OK,
                messageSource.getMessage("file.updated",null, LocaleContextHolder.getLocale()),
                file
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('CANDIDATE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
        ApiResponse<?> apiResponse = new ApiResponse<>(
                HttpStatus.OK,
                messageSource.getMessage("file.deleted",null, LocaleContextHolder.getLocale())
        );
        return  ResponseEntity.ok(apiResponse);
    }
}
