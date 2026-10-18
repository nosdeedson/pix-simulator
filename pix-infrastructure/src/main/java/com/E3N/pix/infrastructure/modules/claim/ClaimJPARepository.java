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

    @Query("SELECT c FROM Claim c " +
            " WHERE ( c.claimerParticipant = ( CASE WHEN :isClaimer = false THEN c.claimerParticipant ELSE :participant END )" +
            " OR c.donorParticipant = (CASE WHEN :isDonor = false THEN c.donorParticipant ELSE :participant END) )" +
            " AND c.status = (CASE WHEN :statusClaim IS NULL THEN c.status ELSE :statusClaim END )  " +
            " AND c.type = (CASE WHEN :typeClaim IS NULL THEN c.type ELSE :typeClaim END ) "
//            " AND c.updatedAt <= (CASE WHEN :modifiedAfter IS NULL THEN c.updatedAt ELSE :modifiedAfter END )" +
//            " AND c.updatedAt >= (CASE WHEN :modifiedBefore IS NULL THEN c.updatedAt ELSE :modifiedBefore END )"
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
