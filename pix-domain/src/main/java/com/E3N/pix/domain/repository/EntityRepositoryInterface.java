package com.E3N.pix.domain.repository;

import java.util.Optional;
import java.util.UUID;

public interface EntityRepositoryInterface<T> extends
        CreateRepositoryInterface<T>,
        ReadRepositoryInterface<T>,
        UpdateRepositoryInterface<T>,
        DeleteRepositoryInterface<T>
{}
