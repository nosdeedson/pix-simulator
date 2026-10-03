package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.validation.Notification;

public class ClaimService {

    public static Claim create(
            final Account acc,
            final EntryKey key,
            final String donorParticipant,
            final TypeClaim typeClaim,
            final Owner claimer
    ) {
        return Claim.getInstance(
                acc.getId().toString(),
                acc.getParticipant().getParticipant(),
                donorParticipant,
                key.getKey().getKey(),
                claimer.getTaxIdNumber().getTaxIdNumber(),
                typeClaim,
                key.getKey().getType(),
                claimer.getType()
        );
    }

    public static Notification validateTypeClaim(Owner claimer, Owner donor, TypeClaim typeClaim, String claimerParticipant) {
        if (TypeClaim.PORTABILITY.equals(typeClaim)
                && claimer.equals(donor)
                && !claimerParticipant.equals(donor.getAccounts().getFirst().getParticipant().getParticipant())
        ){
            return Notification.create("Conflict", 400, "Type of claim must be Ownership");
        }
        return null;
    }
}
