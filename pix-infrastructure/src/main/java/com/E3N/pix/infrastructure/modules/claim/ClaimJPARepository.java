package com.E3N.pix.infrastructure.modules.claim;

import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ClaimJPARepository extends JpaRepository<ClaimJPAEntity, String> {

    // List<Payment> payments = repository.findByStatus("PENDING", PageRequest.of(0, limit)); how to call
    @Query("SELECT c FROM Claim c WHERE " +
            "(:isClaimer = true AND :isDonor = false AND c.claimerParticipant = :participant) OR " +
            "(:isClaimer = true AND :isDonor = true AND (c.claimerParticipant = :participant OR c.donorParticipant = :participant)) OR " +
            "(:isClaimer = false AND :isDonor = true AND c.donorParticipant = :participant) OR " +
            "(:isClaimer = false AND :isDonor = false AND c.claimerParticipant = :participant) " +
            " AND (:statusClaim IS NULL OR c.status = :statusClaim) " +
            " AND (:typeClaim IS NULL OR c.type = :typeClaim) " +
            " AND (:modifiedAfter IS NULL OR c.updatedAt <= :modifiedAfter) " +
            " AND (:modifiedBefore IS NULL OR c.updatedAt >= :modifiedAfter) "
    )
    List<ClaimJPAEntity> findClaims(
            @Param("participant") String participant,
            @Param("isDonor") boolean isDonor,
            @Param("isClaimer") boolean isClaimer,
            @Param("statusClaim") StatusClaim statusClaim,
            @Param("typeClaim") TypeClaim typeClaim,
            @Param("modifiedAfter") final Instant modifiedAfter,
            @Param("modifiedBefore") final Instant modifiedBefore,
            Pageable pageable
    );


}
