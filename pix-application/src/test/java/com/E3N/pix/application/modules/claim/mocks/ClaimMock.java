package com.E3N.pix.application.modules.claim.mocks;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.Owner;

public abstract class ClaimMock {

    public static Claim mockClaim(Owner claimer, Owner donor, TypeClaim typeClaim) {
        return Claim.getInstance(
                claimer.getAccounts().getFirst().getId().toString(),
                claimer.getAccounts().getFirst().getParticipant().getParticipant(),
                claimer.getAccounts().getFirst().getParticipant().getParticipant(),
                claimer.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey(),
                claimer.getTaxIdNumber().getTaxIdNumber(),
                typeClaim,
                donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType(),
                claimer.getType()
        );
    }
}
