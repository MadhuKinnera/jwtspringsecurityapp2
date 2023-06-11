package com.clayfin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clayfin.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

	Optional<AppUser> findByUsername(String username);

}
