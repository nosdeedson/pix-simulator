package com.E3N.pix.domain;

import java.util.Optional;
import java.util.UUID;

public interface EntityRepositoryInterface <T>{
    T save(T entity);
    void delete(UUID uuid);
    Optional<T> findById(UUID uuid);
    // return a pagination and receives a search object
    Optional<T[]> findAll();
    T update(T entity);
}
