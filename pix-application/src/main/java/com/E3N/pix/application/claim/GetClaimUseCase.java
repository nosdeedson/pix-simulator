package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

import java.util.UUID;

public class GetClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public GetClaimUseCase(ClaimRepositoryInterface claimRepository, OwnerRepositoryInterface ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final String idClaim) {
        var optionalClaim = claimRepository.findById(UUID.fromString(idClaim));
        if (optionalClaim.isEmpty()){
            var notification = Notification.create("Not found", 404, "Claim not found.");
            return Either.left(notification);
        }
        var optionalOwner = ownerRepository.findByTaxIdNumber(optionalClaim.get().getTaxIdNumberClaimer().getTaxIdNumber());
        if (optionalOwner.isEmpty()){
            var notification = Notification.create("Not found", 404, "Owner not found.");
            return Either.left(notification);
        }

        return Either.right(ClaimOutputDto.from(optionalClaim.get(), optionalOwner.get()));
    }
}
