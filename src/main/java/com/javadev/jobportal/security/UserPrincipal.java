package com.javadev.jobportal.security;

import com.javadev.jobportal.entity.UserInfo;
import com.javadev.jobportal.enums.UserRole;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private static final Long serialVersionUID = 1L;

    private String username;

    private String password;


    private Set<UserRole> roles;

    public UserPrincipal(UserInfo entity){
        this.username = entity.getEmail();
        this.password  = entity.getPassword();
        this.roles = entity.getRoles();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r->new SimpleGrantedAuthority("ROLE_"+ r.name())).collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, roles);
    }
}
