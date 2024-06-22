package com.codewithsiva.fullstackbackend.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithsiva.fullstackbackend.exception.UserAlreadyExistsException;
import com.codewithsiva.fullstackbackend.exception.UserNotFoundException;
import com.codewithsiva.fullstackbackend.model.RegUsers;
import com.codewithsiva.fullstackbackend.repository.RegUsersRepository;
import com.codewithsiva.fullstackbackend.request.LoginRequest;
import com.codewithsiva.fullstackbackend.response.JwtResponse;
import com.codewithsiva.fullstackbackend.security.jwt.JwtUtils;
import com.codewithsiva.fullstackbackend.security.user.RegUsersDetails;
import com.codewithsiva.fullstackbackend.service.IUserService;

import lombok.RequiredArgsConstructor;

/**
 * @author Simpson Alfred
 */
@RestController
@RequestMapping("/curd")
@RequiredArgsConstructor
public class RegUsersController {
	@Autowired
	private RegUsersRepository regUsersRepository;

	private final IUserService userService;

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@GetMapping("/regusers/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == principal.id)")
	RegUsers getUserById(@PathVariable Long id) {
		return regUsersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

}
