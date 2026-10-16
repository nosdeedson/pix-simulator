package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;

import java.util.Optional;

public class ClaimService {

    public static Claim create(
            final String claimerAccountId,
            final String claimerParticipant,
            final String key,
            final TypeKey typeKey,
            final String donorParticipant,
            final TypeClaim typeClaim,
            final String claimerTaxIdNumber,
            final TypePerson claimerTypePerson
    ) {
        return Claim.getInstance(
                claimerAccountId,
                claimerParticipant,
                donorParticipant,
                key,
                claimerTaxIdNumber,
                typeClaim,
                typeKey,
                claimerTypePerson
        );
    }

    /**
     * Receive claimerParticipant because claimer can have more than one participant
     * must be the same of the request
     *
     * @param claimer @description is an owner
     * @param donor   @description is an owner
     * @return null or Notification
     */
    public static Notification validateTypeClaim(
            Owner claimer, Owner donor, TypeClaim typeClaim,
            String claimerParticipant, TypeKey claimedTypeKey
    ) {
        if (TypeClaim.PORTABILITY.equals(typeClaim)
                && claimer.getTaxIdNumber().getTaxIdNumber().equals(donor.getTaxIdNumber().getTaxIdNumber())
                && !claimerParticipant.equals(donor.getAccounts().getFirst().getParticipant().getParticipant())
                && TypeKey.PHONE.equals(claimedTypeKey)
        ) {
            return Notification.create("Conflict", 400, "Type of claim must be Ownership");
        }
        return null;
    }

    public static Optional<EntryKey> getEntryToDonate(Owner donor, final String entryKey) {
        return donor.getAccounts().getFirst()
                .getEntryKeys().stream()
                .filter(it -> it.getKey().getKey().equals(entryKey))
                .findFirst();
    }
}
