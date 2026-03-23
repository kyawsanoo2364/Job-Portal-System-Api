package com.javadev.jobportal.service;

import com.javadev.jobportal.dto.LoginRequestDTO;
import com.javadev.jobportal.dto.RegisterRequestDTO;
import com.javadev.jobportal.dto.UserInfoDTO;
import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.enums.UserRole;
import com.javadev.jobportal.exceptions.CustomWebServiceException;
import com.javadev.jobportal.repository.UserRepository;
import com.javadev.jobportal.response.ApiResponse;
import com.javadev.jobportal.response.AuthResponse;
import com.javadev.jobportal.security.JwtUtils;
import com.javadev.jobportal.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public AuthResponse authenticate(LoginRequestDTO requestDTO){
        UserInfo user = userService.getUserByEmail(requestDTO.getEmail());
        if(!passwordEncoder.matches(requestDTO.getPassword(),user.getPassword())){
            Locale locale = LocaleContextHolder.getLocale();
            String msg = messageSource.getMessage("invalid.credentials",null,locale);
            throw CustomWebServiceException.of(HttpStatus.UNAUTHORIZED,msg);
        }
        UserDetails userDetails = new UserPrincipal(user);
        String token = jwtUtils.generateToken(userDetails);

        return new AuthResponse(token,
                new UserInfoDTO(user)
                );
    }

    @Transactional
    public ApiResponse<UserInfoDTO> register(RegisterRequestDTO requestDTO){
        Optional<UserInfo> user = userRepository.findByEmail(requestDTO.getEmail());
        if(user.isPresent()){
            throw CustomWebServiceException.of(HttpStatus.CONFLICT,messageSource.getMessage("email.exists",null,LocaleContextHolder.getLocale()));
        }
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(requestDTO.getRole());
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(requestDTO.getEmail());
        userInfo.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        userInfo.setFirstName(requestDTO.getFirstName());
        userInfo.setLastName(requestDTO.getLastName());
        userInfo.setRoles(userRoles);

        UserInfo newUser = userRepository.save(userInfo);

        return new ApiResponse<>(
                HttpStatus.CREATED,
                messageSource.getMessage("user.registered",null,LocaleContextHolder.getLocale()),
                new UserInfoDTO(newUser)
        );

    }

}
