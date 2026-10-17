package com.E3N.pix.domain.modules.claim;

import com.E3N.pix.domain.repository.CRUDRepositoryInterface;

import java.time.Instant;
import java.util.List;

public interface ClaimRepositoryInterface extends CRUDRepositoryInterface<Claim> {
    List<Claim> findClaimsAsClaimer(
            final String participant,
            StatusClaim statusClaim,
            TypeClaim typeClaim,
            final Instant modifiedAfter,
            final Instant modifiedBefore,
            final int limit
    );

    List<Claim> findClaimsAsDonor(
            final String participant,
            StatusClaim statusClaim,
            TypeClaim typeClaim,
            final Instant modifiedAfter,
            final Instant modifiedBefore,
            final int limit
    );
}
