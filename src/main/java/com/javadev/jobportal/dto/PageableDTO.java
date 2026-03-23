package com.javadev.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDTO<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isLast;

    public PageableDTO(List<T> data, Page<?> page) {
        this.data = data;
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isLast = page.isLast();
    }
}
