package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;

import java.time.Instant;
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

    public void confirmClaim(Claim claim, Owner donor, Owner claimer, final String participantRequesting, ClaimOperationReason reason) {
        this.resolutionPeriodNotEnded(reason, claim.getResolutionPeriodEnd(), claim.getDonorParticipant(), participantRequesting, claim.getType());
        if (TypeClaim.OWNERSHIP.equals(claim.getType())
                && ClaimOperationReason.DEFAULT_OPERATION.equals(reason)
                && claim.getResolutionPeriodEnd().isBefore(Instant.now())
                && participantRequesting.equals(claim.getClaimerParticipant())
        ) {
            donor.removingKey(claim.getKeyClaimed());
        } else if (ClaimOperationReason.USER_REQUESTED.equals(reason)
                && participantRequesting.equals(claim.getClaimerParticipant())
        ) {
            claim.updateCompletionPeriod(Instant.now());
            var donatingKey = donor.removingKey(claim.getKeyClaimed());
            transferKey(claimer, donatingKey, claim);
        } else {
            throw new RuntimeException("The operation does not match any requirement.");
        }
    }

    private void transferKey(Owner claimer, EntryKey donatingKey, final Claim claim) {
        for (Account acc : claimer.getAccounts()) {
            if (acc.getId().toString().equals(claim.getClaimerAccountId())) {
                acc.addKey(donatingKey);
            }
        }
        claim.updateStatus(StatusClaim.COMPLETED);
    }

    private void resolutionPeriodNotEnded(
            ClaimOperationReason reason, Instant resolutionPeriodEnd,
            final String donorClaim, final String participantRequesting, TypeClaim typeClaim
    ) {
        if (ClaimOperationReason.DEFAULT_OPERATION.equals(reason)
                && resolutionPeriodEnd.isAfter(Instant.now())
                && donorClaim.equals(participantRequesting)
                && TypeClaim.OWNERSHIP.equals(typeClaim)
        ) {
            throw new RuntimeException("This operation is not allowed, wait until the resolution period finishes");
        }
    }
}
