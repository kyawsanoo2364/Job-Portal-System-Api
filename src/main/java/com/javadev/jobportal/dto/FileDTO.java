package com.javadev.jobportal.dto;

import com.javadev.jobportal.entity.File;
import lombok.Data;

@Data
public class FileDTO {
    private Long id;
    private String name;
    private String path;
    private String type;

    public FileDTO(File entity){
        this.name = entity.getName();
        this.path = entity.getPath();
        this.type = entity.getType();
        this.id = entity.getId();
    }
}
