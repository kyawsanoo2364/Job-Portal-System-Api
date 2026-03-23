package com.javadev.jobportal.response;

import com.javadev.jobportal.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UserInfoDTO user;
}
