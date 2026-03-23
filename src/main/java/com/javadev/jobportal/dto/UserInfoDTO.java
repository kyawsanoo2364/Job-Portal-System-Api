package com.javadev.jobportal.dto;

import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<UserRole> roles;
    private Date createdAt;

    public UserInfoDTO(UserInfo user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles();
        this.createdAt = user.getCreatedAt();
    }
}
