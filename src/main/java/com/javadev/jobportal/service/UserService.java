package com.javadev.jobportal.service;

import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.exceptions.CustomWebServiceException;
import com.javadev.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public UserInfo getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> {
                    Locale locale = LocaleContextHolder.getLocale();
                    String message = messageSource.getMessage("email.notfound", null,"User not found with email:" + email, locale);
                    return CustomWebServiceException.of(HttpStatus.NOT_FOUND, message);
                }
                );
    }


}
