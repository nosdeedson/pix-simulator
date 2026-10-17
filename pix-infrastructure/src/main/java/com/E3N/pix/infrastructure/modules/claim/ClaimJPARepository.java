package com.E3N.pix.infrastructure.modules.claim;

import com.E3N.pix.domain.modules.claim.Claim;
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

    @Query("SELECT c FROM Claim c WHERE claimerParticipant = :participant AND status = :statusClaim " +
            " AND type = :typeClaim and updatedAt >= :modifiedAfter and updatedAt <= :modifiedBefore")
    List<ClaimJPAEntity> findClaimsAsClaimer(
            @Param("participant") String participant,
            @Param("statusClaim") StatusClaim statusClaim,
            @Param("typeClaim") TypeClaim typeClaim,
            @Param("modifiedAfter") final Instant modifiedAfter,
            @Param("modifiedBefore") final Instant modifiedBefore,
            Pageable pageable
    );

    @Query("SELECT c FROM Claim c WHERE donorParticipant = :participant AND status = :statusClaim " +
            " AND type = :typeClaim and updatedAt >= :modifiedAfter or updatedAt <= :modifiedBefore")
    List<ClaimJPAEntity> findClaimsAsDonor(
            @Param("participant") final String participant,
            @Param("statusClaim") StatusClaim statusClaim,
            @Param("typeClaim") TypeClaim typeClaim,
            @Param("modifiedAfter") final Instant modifiedAfter,
            @Param("modifiedBefore") final Instant modifiedBefore,
            Pageable pageable
    );

}
