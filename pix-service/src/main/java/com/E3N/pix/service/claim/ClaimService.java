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
import java.util.IllegalFormatCodePointException;
import java.util.Optional;

public class ClaimService {

    static final String DONOR_CANNOT = "Donor cannot cancel this type of claim.";
    static final String CLAIMER_CANNOT = "Claimer cannot cancel this type of claim.";
    static final String CLAIM_STATUS_NOT_ALLOW = "Claim's status does not permit to cancel.";
    static final String RESOLUTION_PERIOD_NOT_ENDED = "Claim's resolution period must have ended to cancel as default operation.";

    public Claim create(
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
    public Notification validateTypeClaim(
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

    public Optional<EntryKey> getEntryToDonate(Owner donor, final String entryKey) {
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

    public Notification cannotCancel(final Claim claim, ClaimOperationReason reason, final String participantRequest) {
        // ok
        if (ClaimOperationReason.PARTICIPANT_EXCLUSION.equals(reason)){
            return Notification.create("Conflict", 409, "The use of 'PARTICIPANT_EXCLUSION is exclusive for DICT.");
        }
        // ok
        if (claim.getType().equals(TypeClaim.PORTABILITY)
                && (!claim.getStatus().equals(StatusClaim.OPEN)
                && !claim.getStatus().equals(StatusClaim.WAITING_RESOLUTION))
                && claim.getClaimerParticipant().equals(participantRequest)
        ) {
            return Notification.create("Conflict", 409, CLAIM_STATUS_NOT_ALLOW);
        }
        // ok
        if (reason.equals(ClaimOperationReason.DEFAULT_OPERATION) && Instant.now().isBefore(claim.getResolutionPeriodEnd())
        && claim.getClaimerParticipant().equals(participantRequest)) {
            return Notification.create("Conflict", 409, RESOLUTION_PERIOD_NOT_ENDED);
        }
        // ok
        if (ClaimOperationReason.ACCOUNT_CLOSURE.equals(reason) && claim.getDonorParticipant().equals(participantRequest)) {
            return Notification.create("Conflict", 409, DONOR_CANNOT );
        }
        // ok
        if (ClaimOperationReason.DEFAULT_OPERATION.equals(reason) && claim.getClaimerParticipant().equals(participantRequest)) {
            return Notification.create("Conflict", 409, CLAIMER_CANNOT);
        }
        // ok
        if (claim.getDonorParticipant().equals(participantRequest) &&
                (ClaimOperationReason.RECONCILIATION.equals(reason) || ClaimOperationReason.RFB_VALIDATION.equals(reason))) {
            return Notification.create("Conflict", 409, DONOR_CANNOT);
        }
        if (claim.getStatus().equals(StatusClaim.CONFIRMED)
                && claim.getDonorParticipant().equals(participantRequest)
                && (
                        ClaimOperationReason.FRAUD.equals(reason)
                        || ClaimOperationReason.RECONCILIATION.equals(reason)
                        || ClaimOperationReason.RFB_VALIDATION.equals(reason)
                    )
        ){
            return Notification.create("Conflict", 409, DONOR_CANNOT);
        }
        if (claim.getStatus().equals(StatusClaim.WAITING_RESOLUTION) && ClaimOperationReason.RECONCILIATION.equals(reason)){
            return Notification.create("Conflict", 409, CLAIM_STATUS_NOT_ALLOW);
        }
        if (claim.getType().equals(TypeClaim.OWNERSHIP)
                && (
                        !claim.getStatus().equals(StatusClaim.WAITING_RESOLUTION)
                        || !claim.getStatus().equals(StatusClaim.OPEN)
                        || !claim.getStatus().equals(StatusClaim.CONFIRMED)
                    )
        ){
            return Notification.create("Conflict", 409, CLAIM_STATUS_NOT_ALLOW);
        }
        if (claim.getType().equals(TypeClaim.OWNERSHIP) && claim.getDonorParticipant().equals(participantRequest)
                && !ClaimOperationReason.FRAUD.equals(reason)
        ){
            return Notification.create("Conflict", 409, DONOR_CANNOT);
        }
        return null;
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