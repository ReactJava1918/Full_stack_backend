package com.codewithsiva.fullstackbackend.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codewithsiva.fullstackbackend.model.RegUsers;
import com.codewithsiva.fullstackbackend.repository.RegUsersRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Simpson Alfred
 */
@Service
@RequiredArgsConstructor
public class RegUsersDetailsService implements UserDetailsService {
    private final RegUsersRepository regUsersRepository;

    @Override
    public UserDetails loadUserByUsername(String email)   {
        RegUsers reguser = regUsersRepository.findByEmail(email);
        if (reguser != null) {
            return RegUsersDetails.buildUserDetails(reguser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
