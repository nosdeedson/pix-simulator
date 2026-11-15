package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;

import java.time.Instant;
import java.util.UUID;

public class CancelClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public CancelClaimUseCase(ClaimRepositoryInterface claimRepository, OwnerRepositoryInterface ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final String claimId, ClaimOperationReason reason, final String participantRequest){
        var optionalClaim = claimRepository.findById(UUID.fromString(claimId));
        if (optionalClaim.isEmpty()){
            return Either.left(Notification.create("Not found", 404, "Claim not found."));
        }
        var claim = optionalClaim.get();
        var service = new ClaimService();
        if (service.canCancel(claim, reason, participantRequest)){
            claim.updateStatus(StatusClaim.CANCELLED);
            claimRepository.update(claim);
            Owner whoAsking = ownerRepository.findByKey(claim.getKeyClaimed()).orElseThrow();
            var output = ClaimOutputDto.from(claim, whoAsking);
            return Either.right(output);
        }
        return Either.left(Notification.create("Conflict", 409, "Participant is not allowed to access this resource."));
    }
}
