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
        List<Claim> claims = claimRepository.findClaims(
                dto.participant(),
                dto.isDonor(),
                dto.isClaimer(),
                dto.statusClaim(),
                dto.typeClaim(),
                dto.modifiedAfter(),
                dto.modifiedAfter(),
                dto.limit()
        );
        if (claims.isEmpty())
            return Either.left(Notification.create("Not found", 404, "There is no claims."));

        List<ClaimOutputDto> outputDTOs = new ArrayList<>(claims.size());
        for (Claim c : claims) {
            var optionalAccount = ownerRepository
                    .findAccount(c.getClaimerAccountId(), c.getTaxIdNumberClaimer().getTaxIdNumber());
            if (optionalAccount.isEmpty())
                return Either.left(Notification.create("Not found.", 404, "Claimer not found."));
            var account = optionalAccount.get();

            var optionalEntryKey = ownerRepository
                    .findEntryKey(c.getKeyClaimed(), account.getNumber().getNumber(), c.getTaxIdNumberClaimer().getTaxIdNumber());
            if (optionalEntryKey.isEmpty())
                return Either.left(Notification.create("Not found.", 404, "Claimer not found."));
            var key = optionalEntryKey.get();

            var optionalOwner = ownerRepository
                    .findByTaxIdNumber(c.getTaxIdNumberClaimer().getTaxIdNumber());
            if (optionalOwner.isEmpty())
                return Either.left(Notification.create("Not found.", 404, "Claimer not found."));
            var owner = optionalOwner.get();

            var output = ClaimOutputDto.from(account, key, c, owner);
            outputDTOs.add(output);
        }
        return Either.right(outputDTOs);
    }
}
