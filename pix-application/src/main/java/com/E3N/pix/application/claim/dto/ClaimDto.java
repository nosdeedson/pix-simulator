package com.E3N.pix.application.claim.dto;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;

public record ClaimDto(
        TypeClaim typeClaim,
        String key,
        TypeKey typeKey,
        ClaimerAccountDto claimerAccountDto,
        ClaimerDto claimerDto
) {
}
