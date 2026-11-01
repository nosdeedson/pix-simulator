package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;

import java.util.UUID;

public class ConfirmClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public ConfirmClaimUseCase(ClaimRepositoryInterface claimRepository, OwnerRepositoryInterface ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final String claimId, final String participant, ClaimOperationReason reason) {
        var optionalClaim = claimRepository.findById(UUID.fromString(claimId));
        if (optionalClaim.isEmpty()) {
            return Either.left(Notification.create("Not found", 404, "Claim not found."));
        }
        var claim = optionalClaim.get();
        if (!StatusClaim.WAITING_RESOLUTION.equals(claim.getStatus())) {
            return Either.left(Notification
                    .create("Not found", 404, "The status of claim does not permit this operation.")
            );
        }
        var claimService = new ClaimService();
        var optionalDonor = ownerRepository.findByKey(claim.getKeyClaimed());
        if (optionalDonor.isEmpty()) {
            return Either.left(Notification.create("Not found", 404, "Donor not found."));
        }
        var donor = optionalDonor.get();
        claimService.confirmClaim(claim, donor, participant, reason);
        var claimer = ownerRepository.findByTaxIdNumber(claim.getTaxIdNumberClaimer().toString()).orElseThrow();
        return Either.right(ClaimOutputDto.from(claim, claimer));
    }
}
