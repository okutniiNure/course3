package io.swagger.repository;

import io.swagger.model.AccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccessGroupRepository extends JpaRepository<AccessGroup, String> {
}
