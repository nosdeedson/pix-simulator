package com.E3N.pix.infrastructure.modules.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerJPARepository extends JpaRepository<OwnerJPAEntity, String> {
    @Query("SELECT o FROM Owner o " +
            "JOIN FETCH o.accounts " +
            "WHERE o.taxIdNumber = :taxIdNumber")
    Optional<OwnerJPAEntity> findByTaxIdNumber(@Param("taxIdNumber") String taxIdNumber);

    @Query("SELECT o FROM Owner o JOIN FETCH o.accounts a JOIN FETCH a.keyJPAs ek WHERE ek.key = :key")
    Optional<OwnerJPAEntity> findByKey(@Param("key") String key);
}
