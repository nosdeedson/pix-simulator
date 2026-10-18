package com.E3N.pix.infrastructure.modules.ownership.owner;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.infrastructure.modules.ownership.account.AccountJPAEntity;
import com.E3N.pix.infrastructure.modules.ownership.entryKey.EntryKeyJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerJPARepository extends JpaRepository<OwnerJPAEntity, String> {

    @Query("SELECT a FROM Owner o JOIN o.accounts a JOIN FETCH a.keyJPAs ek " +
            " WHERE ek.key = :key AND a.id = :accountId")
    Optional<AccountJPAEntity> findAccount(@Param("accountId") String accountId, @Param("key") String key);

    @Query("SELECT o FROM Owner o " +
            "JOIN FETCH o.accounts " +
            "WHERE o.taxIdNumber = :taxIdNumber")
    Optional<OwnerJPAEntity> findByTaxIdNumber(@Param("taxIdNumber") String taxIdNumber);

    // BRINGS ALL KEYS OF ACCOUNT
    @Query("SELECT o FROM Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek WHERE ek.key = :key")
    Optional<OwnerJPAEntity> findByKey(@Param("key") String key);

    @Query("SELECT ek FROM entry_key ek JOIN FETCH ek.account a JOIN a.owner o" +
            " WHERE ek.key = :key AND a.number = :numberAccount AND o.taxIdNumber = :taxIdNumber")
    Optional<EntryKeyJPAEntity> findEntryKey(
            @Param("key") String key,
            @Param("numberAccount") String numberAccount,
            @Param("taxIdNumber") String taxIdNumber
    );

    @Query("SELECT o from Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek " +
            " WHERE ek.key = :key AND o.taxIdNumber = :taxIdNumber")
    Optional<OwnerJPAEntity> findByKeyAndTaxIdNumber(@Param("key") String key, @Param("taxIdNumber") String taxIdNumber);

    @Query("SELECT o from Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek " +
            " WHERE ek.key = :key AND a.participant = :participant")
    Optional<OwnerJPAEntity> findByKeyAndParticipant(@Param("key") String key, @Param("participant") String participant);

    @Query("SELECT ek.key FROM Owner o JOIN o.accounts a JOIN a.keyJPAs ek WHERE ek.key IN :keys")
    List<String> findByKeys(@Param("keys") List<String> keys);

    @Query("SELECT o FROM Owner o JOIN FETCH o.accounts a JOIN a.keyJPAs ek " +
            " WHERE a.number = :accountNumber AND a.participant = :participant " +
            " AND a.branch = :branch AND o.taxIdNumber = :taxIdNumber")
    Optional<OwnerJPAEntity> findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
            @Param("accountNumber") String accountNumber, @Param("participant") String participant,
            @Param("branch") String branch, @Param("taxIdNumber") String taxIdNumber);
}
