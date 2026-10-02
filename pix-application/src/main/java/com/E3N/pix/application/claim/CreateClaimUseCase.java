package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.ClaimDto;
import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;

public class CreateClaimUseCase {

    private ClaimRepositoryInterface claimRepository;
    private OwnerRepositoryInterface ownerRepository;

    public CreateClaimUseCase(
            ClaimRepositoryInterface claimRepository,
            OwnerRepositoryInterface ownerRepository
    ) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, Claim> execute(final ClaimDto dto) {
        var claimer = ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                dto.claimerAccountDto().accountNumber(), dto.claimerAccountDto().participant(),
                dto.claimerAccountDto().branch(), dto.claimerDto().taxIdNumber()
        );
        if (claimer.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "Claimer not found");
            return Either.left(notification);
        }
        Account acc = claimer.get().getAccounts().stream()
                .filter(it -> it.getNumber().getNumber().equals(dto.claimerAccountDto().accountNumber()))
                .findAny().orElse(null);
        EntryKey key = null;
        if (acc != null) {
            key = acc.getEntryKeys().stream().filter(it -> it.getKey().getKey().equals(dto.key())).findAny()
                    .orElse(null);
        }
        var donor = ownerRepository.findByKey(dto.key());
        if (donor.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "Donor not found");
            return Either.left(notification);
        }
        var claim = ClaimService.create(
                acc,
                key,
                donor.get().getAccounts().getFirst().getParticipant().getParticipant(),
                dto.typeClaim(),
                claimer.get()
        );
        return null;
    }
}
