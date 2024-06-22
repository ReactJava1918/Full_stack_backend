package com.codewithsiva.fullstackbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithsiva.fullstackbackend.model.RegUsers;

public interface RegUsersRepository extends JpaRepository<RegUsers,Long> {

	boolean existsByEmail(String email);
	RegUsers findByEmail(String email);
	
}
