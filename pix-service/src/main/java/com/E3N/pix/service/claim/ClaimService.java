package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.service.claim.dto.ClaimDto;

public class ClaimService {

    public Claim create(final ClaimDto dto) {
        return Claim.getInstance(
                dto.accountId(),
                dto.claimerParticipant(),
                dto.donorParticipant(),
                dto.keyClaimed(),
                dto.taxIdClaimer(),
                dto.typeClaim(),
                dto.typeKey(),
                dto.typePerson()
        );
    }
}
