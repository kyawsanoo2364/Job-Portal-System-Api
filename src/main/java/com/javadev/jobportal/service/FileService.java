package com.javadev.jobportal.service;

import com.javadev.jobportal.dto.FileDTO;
import com.javadev.jobportal.dto.PageableDTO;
import com.javadev.jobportal.entity.File;
import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.exceptions.CustomWebServiceException;
import com.javadev.jobportal.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MessageSource messageSource;
    private final FileRepository fileRepository;
    private final UserService userService;

    @Value("${local.upload.dir:/app/uploads/files}")
    private String uploadDir;

    public FileDTO uploadFile(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }
        UserInfo user = userService.getUserByEmail(authentication.getName());
        if(file.isEmpty()) {
            throw CustomWebServiceException.of(
                    HttpStatus.BAD_REQUEST,
                    messageSource.getMessage("file.empty",null, LocaleContextHolder.getLocale())
            );
        }
        try{
            byte[] bytes = file.getBytes();

            Path path = Paths.get(uploadDir);
            if(!Files.exists(path)){
                Files.createDirectories(path);
            }
            Path filePath = path.resolve(file.getOriginalFilename());
            Files.write(filePath, bytes);

            File dbFile = File.builder()
                    .type(file.getContentType())
                    .name(file.getOriginalFilename()+"_"+new Date().getTime())
                    .path("/uploads/files/" + file.getOriginalFilename())
                    .user(user)
                    .build();

            File saved = fileRepository.save(dbFile);
            return new FileDTO(saved);
        } catch (Exception e) {
            throw CustomWebServiceException.of(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("file.upload.failed",null, LocaleContextHolder.getLocale())
            );
        }


    }

    public PageableDTO<?> getAll(Pageable pageable){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }
        UserInfo user = userService.getUserByEmail(authentication.getName());
        Page<File> page = fileRepository.findByUser(user, pageable);
        List<FileDTO> fileDTOS = page.getContent().stream().map(FileDTO::new).toList();

        return new PageableDTO<>(fileDTOS,page);
    }

    public File findById(Long id){
        return fileRepository.findById(id).orElseThrow(()->
                CustomWebServiceException.of(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage("file.notfound",null, LocaleContextHolder.getLocale())
                )
        );
    }

    public FileDTO getById(Long id){
        File existsFile =  this.findById(id);
        return new FileDTO(existsFile);
    }

    public FileDTO updateFile(Long id, String filename){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }
        UserInfo user = userService.getUserByEmail(authentication.getName());

        File existsFile =this.findById(id);
        if(!existsFile.getUser().equals(user)){
            throw CustomWebServiceException.of(
                    HttpStatus.FORBIDDEN,
                    messageSource.getMessage("file.forbidden",null, LocaleContextHolder.getLocale())
            );
        }
        existsFile.setName(filename);

        File saved = fileRepository.save(existsFile);
        return new FileDTO(saved);
    }


    public void deleteFile(Long id) throws IOException {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,messageSource.getMessage("unauthenticated",null, LocaleContextHolder.getLocale()));
        }
        UserInfo user = userService.getUserByEmail(authentication.getName());
        File existsFile = this.findById(id);
        if(!existsFile.getUser().equals(user)){
            throw CustomWebServiceException.of(
                    HttpStatus.FORBIDDEN,
                    messageSource.getMessage("file.forbidden",null, LocaleContextHolder.getLocale())
            );
        }
        Path filePath = Paths.get("."+existsFile.getPath());
        Files.deleteIfExists(filePath);
        fileRepository.delete(existsFile);
    }

    public void deleteFile(Long id,boolean isJobDelete) throws IOException {
        File existsFile = this.findById(id);
        Path filePath = Paths.get("."+existsFile.getPath());
        Files.deleteIfExists(filePath);
        fileRepository.delete(existsFile);
    }
}
