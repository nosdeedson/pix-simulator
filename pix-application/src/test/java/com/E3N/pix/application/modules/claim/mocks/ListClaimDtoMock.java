package com.E3N.pix.application.modules.claim.mocks;

import com.E3N.pix.application.claim.dto.ListClaim.ListClaimDto;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.test.Owner.RandomParticipant;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class ListClaimDtoMock {

    public static ListClaimDto getDto() {
        return new ListClaimDto(
                RandomParticipant.getParticipant(),
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                StatusClaim.OPEN,
                TypeClaim.PORTABILITY,
                Instant.now().plus(10, ChronoUnit.DAYS),
                Instant.now().minus(10, ChronoUnit.DAYS),
                20
        );
    }
}
