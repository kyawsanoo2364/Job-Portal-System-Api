package com.javadev.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path",nullable = false)
    private String path;

    @Column(name = "type",nullable = false)
    private String type;

    @OneToMany(mappedBy = "file",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<JobApplication> jobApplications;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        File file = (File) o;
        return Objects.equals(name, file.name) && Objects.equals(path, file.path) && Objects.equals(type, file.type) && Objects.equals(user, file.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, path, type, user);
    }
}
