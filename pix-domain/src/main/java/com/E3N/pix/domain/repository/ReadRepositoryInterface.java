package com.E3N.pix.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface ReadRepositoryInterface <T>{
    Optional<T> findById(UUID id);
    Optional<T[]> findAll();
}
