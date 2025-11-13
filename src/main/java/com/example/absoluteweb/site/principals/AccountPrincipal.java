package com.example.absoluteweb.site.principals;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AccountPrincipal(String login, String role) implements UserDetails {

    @Override public String getUsername() { return login; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    public String getRole() { return role; }

    /* Інші методи UserDetails – можна повернути true/"" тощо, якщо вони вам не потрібні */
    @Override public String getPassword() { return ""; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
