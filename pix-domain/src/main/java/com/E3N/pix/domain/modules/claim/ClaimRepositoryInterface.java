package com.E3N.pix.domain.modules.claim;

import com.E3N.pix.domain.repository.CRUDRepositoryInterface;

import java.time.Instant;
import java.util.List;

public interface ClaimRepositoryInterface extends CRUDRepositoryInterface<Claim> {
    List<Claim> findClaims(
            final String participant,
            boolean isDonor,
            boolean isClaimer,
            StatusClaim statusClaim,
            TypeClaim typeClaim,
            Instant modifiedAfter,
            Instant modifiedBefore,
            int limit
    );

}
