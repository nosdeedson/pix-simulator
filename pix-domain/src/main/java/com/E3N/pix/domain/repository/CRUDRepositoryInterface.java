package com.E3N.pix.domain.repository;

public interface CRUDRepositoryInterface<T> extends
        CreateRepositoryInterface<T>,
        ReadRepositoryInterface<T>,
        UpdateRepositoryInterface<T>,
        DeleteRepositoryInterface<T> {
}
