package com.javadev.jobportal.entity;

import com.javadev.jobportal.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserInfo extends BaseEntity {
    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable=false)
    private String password;

    @Column(name = "roles",nullable=false)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Column(name = "job_applications")
    private List<JobApplication> jobApplications;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @Column(name = "jobs")
    private List<Job> jobs;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(firstName, userInfo.firstName) && Objects.equals(lastName, userInfo.lastName) && Objects.equals(email, userInfo.email) && Objects.equals(password, userInfo.password) && Objects.equals(roles, userInfo.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, password, roles);
    }
}
