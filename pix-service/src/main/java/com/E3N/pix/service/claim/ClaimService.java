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

    static final String CLAIM_STATUS_NOT_ALLOW = "Claim's status %s does not permit cancellation or you are not allowed.";

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

    /**
     * Case: 'FRAUD, RECONCILIATION or RFB_VALIDATION' claimer can cancel portability if status is 'CONFIRMED'.
     * Case: 'FRAUD' donor can cancel OWNERSHIP.
     *
     * @param claim              @description claim being canceled
     * @param reason             @description why the claim is being canceled
     * @param participantRequest @description who is canceling
     * @return 'null' when can cancel or a Notification with the detail
     */
    public boolean canCancel(final Claim claim, ClaimOperationReason reason, final String participantRequest) {
        // VALIDATE PARTICIPANT_EXCLUSION
        if (ClaimOperationReason.PARTICIPANT_EXCLUSION.equals(reason)){
            return false;
        }
        if (claim.getType().equals(TypeClaim.PORTABILITY)) {
            // Validate claim status (OPEN, WAITING_RESOLUTION) CASE (USER_REQUESTED, FRAUD) CLAIMER AND DONOR CAN CANCEL
            if ( ( StatusClaim.OPEN.equals(claim.getStatus()) || StatusClaim.WAITING_RESOLUTION.equals(claim.getStatus()) )
                            && ( ClaimOperationReason.USER_REQUESTED.equals(reason) || ClaimOperationReason.FRAUD.equals(reason) )
            ) {
                return true;
            }
            // Validate status claim (WAITING_RESOLUTION) reason RECONCILIATION can not cancel claim
            else if (StatusClaim.WAITING_RESOLUTION.equals(claim.getStatus()) && ClaimOperationReason.RECONCILIATION.equals(reason)){
                return false;
            }
            // Case reason (ACCOUNT_CLOSURE, RECONCILIATION, RF_VALIDATION) Claimer can cancel claim
            else if ( (ClaimOperationReason.ACCOUNT_CLOSURE.equals(reason)
                || ClaimOperationReason.RECONCILIATION.equals(reason)
                || ClaimOperationReason.RFB_VALIDATION.equals(reason))
                    && claim.isClaimer(participantRequest)
            ) {
                return true;
            }
            // Case reason DEFAULT_OPERATION donor can cancel claim
            else if (ClaimOperationReason.DEFAULT_OPERATION.equals(reason) && claim.isDonor(participantRequest) ){
                return true;
            }
            // Case reason (FRAUD, RFB_VALIDATION, RECONCILIATION) and status claim is confirmed
            else if ( (ClaimOperationReason.FRAUD.equals(reason)
                    || ClaimOperationReason.RFB_VALIDATION.equals(reason)
                    || ClaimOperationReason.RECONCILIATION.equals(reason) )
                    && StatusClaim.CONFIRMED.equals(claim.getStatus())
                    && claim.isClaimer(participantRequest)
            ){
                return true;
            }
        }
        if (TypeClaim.OWNERSHIP.equals(claim.getType()) && claim.isClaimer(participantRequest)){
            // Case status claim is (OPEN, WAITING_RESOLUTION, CONFIRMED) when OWNERSHIP just claimer can cancel
            // if reason is DEFAULT_OPERATION completion period must have expired
            return StatusClaim.OPEN.equals(claim.getStatus())
                    || StatusClaim.WAITING_RESOLUTION.equals(claim.getStatus())
                    || StatusClaim.CONFIRMED.equals(claim.getStatus())
                    || (ClaimOperationReason.DEFAULT_OPERATION.equals(reason) && claim.completionPeriodEnded());
        }

        return claim.isDonor(participantRequest) && ClaimOperationReason.FRAUD.equals(reason)
                && !claim.getStatus().equals(StatusClaim.CONFIRMED);
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