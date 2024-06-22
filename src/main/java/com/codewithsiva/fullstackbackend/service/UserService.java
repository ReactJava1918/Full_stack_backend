package com.codewithsiva.fullstackbackend.service;

import java.util.Collections;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithsiva.fullstackbackend.exception.UserAlreadyExistsException;
import com.codewithsiva.fullstackbackend.model.RegUsers;
import com.codewithsiva.fullstackbackend.model.Role;
import com.codewithsiva.fullstackbackend.repository.RegUsersRepository;
import com.codewithsiva.fullstackbackend.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Simpson Alfred
 */

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final RegUsersRepository regUsersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public RegUsers registerUser(RegUsers regUser) {
        if (regUsersRepository.existsByEmail(regUser.getEmail())){
            throw new UserAlreadyExistsException(regUser.getEmail() + " already exists");
        }
        regUser.setPassword(passwordEncoder.encode(regUser.getPassword()));
        System.out.println(regUser.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        regUser.setRoles(Collections.singletonList(userRole));
        return regUsersRepository.save(regUser);
    }

    
}
