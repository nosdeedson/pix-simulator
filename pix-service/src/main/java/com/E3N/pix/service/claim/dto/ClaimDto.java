package com.E3N.pix.service.claim.dto;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;

public record ClaimDto(
        String accountId,
        String claimerParticipant,
        String donorParticipant,
        String keyClaimed,
        String taxIdClaimer,
        TypeClaim typeClaim,
        TypeKey typeKey,
        TypePerson typePerson
) {
}
