package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;

import java.util.UUID;

public class CancelClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;

    public CancelClaimUseCase(ClaimRepositoryInterface claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final String claimId, final String participantRequest, ClaimOperationReason reason){
        var optionalClaim = claimRepository.findById(UUID.fromString(claimId));
        if (optionalClaim.isEmpty()){
            return Either.left(Notification.create("Not found", 404, "Claim not found."));
        }
        var claim = optionalClaim.get();
        var service = new ClaimService();
        Notification notification = null;
        if (( notification = service.cannotCancel(claim, reason, participantRequest)) != null ){
            return Either.left(notification);
        }
        return null;
    }
}
