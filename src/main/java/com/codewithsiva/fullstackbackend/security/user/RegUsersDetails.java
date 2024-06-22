package com.codewithsiva.fullstackbackend.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.codewithsiva.fullstackbackend.model.RegUsers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Simpson Alfred
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegUsersDetails implements UserDetails {
    private Long id;
    private  String email;
    private String password;
    private Collection<GrantedAuthority> authorities;
    private String profileImg;

    public static RegUsersDetails buildUserDetails(RegUsers regUsers){
        List<GrantedAuthority> authorities = regUsers.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new RegUsersDetails(
        		regUsers.getId(),
        		regUsers.getEmail(),
        		regUsers.getPassword(),
                authorities,
                regUsers.getProfileImg()
                );

    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
}
