package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

import java.util.UUID;

public class AcknowledgeClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public AcknowledgeClaimUseCase(ClaimRepositoryInterface claimRepository, OwnerRepositoryInterface ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final String claimId, final String participant){
        var optionalClaim = this.claimRepository.findById(UUID.fromString(claimId));
        if (optionalClaim.isEmpty()){
            return Either.left(Notification.create("Not found", 404, "Claim not found."));
        }
        var claim = optionalClaim.get();
        if (!claim.getClaimerParticipant().equals(participant)){
            return Either.left(Notification.create("Not found", 404, "Claim belongs to another participant."));
        }
        // TODO The operation is idempotence. If claim exist and was already answered and the status is
        //  WAITING_RESOLUTION, should return the same answer of the first.
        var optionalClaimer = ownerRepository.findByTaxIdNumber(claim.getTaxIdNumberClaimer().getTaxIdNumber());
        return optionalClaimer.<Either<Notification, ClaimOutputDto>>map(owner -> Either.right(ClaimOutputDto.from(claim, owner)))
                .orElseGet(() -> Either.left(Notification.create("Not found", 404, "Claimer not found.")));
    }
}
