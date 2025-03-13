package com.example.teleconsultation.security;

import com.example.teleconsultation.models.Utilisateur;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final String email;
    private final String password;

    public UserDetailsImpl(Utilisateur user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public static UserDetailsImpl build(Utilisateur user) {
        return new UserDetailsImpl(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
