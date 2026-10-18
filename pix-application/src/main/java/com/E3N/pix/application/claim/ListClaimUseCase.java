package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.ListClaim.ListClaimDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;

import java.util.ArrayList;
import java.util.List;

public class ListClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public ListClaimUseCase(ClaimRepositoryInterface claimRepository, OwnerRepositoryInterface ownerRepository) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, List<ClaimOutputDto>> execute(final ListClaimDto dto) {
        List<Claim> claims = new ArrayList<>();
        if (dto.isClaimer()){
            claims = claimRepository.findClaimsAsClaimer(dto.participant(), dto.statusClaim(), dto.typeClaim(), dto.modifiedAfter(), dto.modifiedBefore(), dto.limit());
        } else if (dto.isDonor()){
            claims = claimRepository.findClaimsAsClaimer(dto.participant(), dto.statusClaim(), dto.typeClaim(), dto.modifiedAfter(), dto.modifiedBefore(), dto.limit());
        } else {
            claims = claimRepository.findClaimsAsClaimer(dto.participant(), null, null, null, null, 20);
        }
        if (claims.isEmpty()){
            return Either.left(Notification.create("Not found", 404, "There is no claims"));
        }
        List<ClaimOutputDto> outputDtos = new ArrayList<>(claims.size());
        for (Claim c : claims){

        }
        return null;
    }
}
