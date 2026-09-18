package com.E3N.pix.infrastructure.modules.ownership.owner;

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

    // BRINGS ALL KEYS OF ACCOUNT
    @Query("SELECT o FROM Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek WHERE ek.key = :key")
    Optional<OwnerJPAEntity> findByKey(@Param("key") String key);

//    @Query("SELECT DISTINCT ek FROM entry_key ek JOIN FETCH ek.account a JOIN FETCH a.owner WHERE ek.key = :key")
//    Optional<EntryKeyJPAEntity> findFromKey(@Param("key") String key);

    @Query("SELECT o from Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek " +
            " WHERE ek.key = :key AND o.taxIdNumber = :taxIdNumber")
    Optional<OwnerJPAEntity> findByKeyAndTaxIdNumber(@Param("key") String key, @Param("taxIdNumber") String taxIdNumber);

    @Query("SELECT o from Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek " +
            " WHERE ek.key = :key AND a.participant = :participant")
    Optional<OwnerJPAEntity> findByKeyAndParticipant(@Param("key") String key, @Param("participant") String participant);
}
