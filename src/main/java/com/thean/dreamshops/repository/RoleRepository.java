package com.thean.dreamshops.repository;

import com.thean.dreamshops.dto.RoleDTO;
import com.thean.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}
