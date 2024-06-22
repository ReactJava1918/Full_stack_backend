package com.codewithsiva.fullstackbackend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import org.apache.commons.io.FileUtils;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private RegUsersRepository regUsersRepository;

	private final IUserService userService;

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody RegUsers regusers) {
		try {
			userService.registerUser(regusers);
			return ResponseEntity.ok("Registration successful!");

		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) throws IOException {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtTokenForUser(authentication);
		RegUsersDetails userDetails = (RegUsersDetails) authentication.getPrincipal();
		
		File image = new File(System.getProperty("user.dir")+File.separator+"profileImgs"+File.separator+userDetails.getProfileImg());
		String dataUrl = "";
		if(image.exists()) {
			byte[] imageBytes = FileUtils.readFileToByteArray(image);
			 String baseEncodedStr= Base64.getEncoder().encodeToString(imageBytes);
			  dataUrl = "data:image/"+(userDetails.getProfileImg().split("\\."))[1]+";base64,"+baseEncodedStr;
		   
		} 
		
		
		
		List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		return ResponseEntity.ok(new JwtResponse(userDetails.getId(), userDetails.getEmail(), jwt, roles,dataUrl));
	}

	@GetMapping("/regusers/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == principal.id)")
	RegUsers getUserById(@PathVariable Long id) {
		return regUsersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@GetMapping({ "/regusers" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<RegUsers> getAllUsers() {
		System.out.println("cntrl entered here...........");
		return this.regUsersRepository.findAll();
	}

	@PutMapping({ "/regusers/{id}" })
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == principal.id)")
	RegUsers updateRegUsers(@RequestBody RegUsers regUsers, @PathVariable Long id) {
		return (RegUsers) this.regUsersRepository.findById(id).map((regUserDb) -> {
			regUserDb.setFirstName(regUsers.getFirstName());
			regUserDb.setLastName(regUsers.getLastName());
			regUserDb.setEmail(regUsers.getEmail());
			return (RegUsers) this.regUsersRepository.save(regUserDb);
		}).orElseThrow(() -> {
			return new UserNotFoundException(id);
		});
	}

	@DeleteMapping({ "/regusers/{id}" })
	@PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == principal.id)")
	String deleteUser(@PathVariable Long id) {
		if (!this.regUsersRepository.existsById(id)) {
			throw new UserNotFoundException(id);
		} else {
			this.regUsersRepository.deleteById(id);
			return "User with id " + id + " has been deleted success.";
		}
	}

}
