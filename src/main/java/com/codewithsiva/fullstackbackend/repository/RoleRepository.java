package com.codewithsiva.fullstackbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithsiva.fullstackbackend.model.Role;

/**
 * @author Simpson Alfred
 */

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String role);


    boolean existsByName(String role);
}
