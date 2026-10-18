package com.E3N.pix.application.claim.dto.ListClaim;

import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;

import java.time.Instant;

public record ListClaimDto(
        String participant,
        boolean includeIndirectParticipant,
        boolean isDonor,
        boolean isClaimer,
        StatusClaim statusClaim,
        TypeClaim typeClaim,
        Instant modifiedAfter,
        Instant modifiedBefore,
        int limit
) {
}
