package com.E3N.pix.application.claim;

import com.E3N.pix.application.claim.dto.ClaimDto;
import com.E3N.pix.application.claim.dto.ClaimOutputDto;
import com.E3N.pix.domain.modules.claim.ClaimRepositoryInterface;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.OwnerRepositoryInterface;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.Either;
import com.E3N.pix.service.claim.ClaimService;

import java.util.Optional;

public class CreateClaimUseCase {

    private final ClaimRepositoryInterface claimRepository;
    private final OwnerRepositoryInterface ownerRepository;

    public CreateClaimUseCase(
            ClaimRepositoryInterface claimRepository,
            OwnerRepositoryInterface ownerRepository
    ) {
        this.claimRepository = claimRepository;
        this.ownerRepository = ownerRepository;
    }

    public Either<Notification, ClaimOutputDto> execute(final ClaimDto dto) {
        var claimer = ownerRepository.findByAccountNumberAndParticipantAndBranchAndTaxIdNumber(
                dto.claimerAccountDto().accountNumber(), dto.claimerAccountDto().participant(),
                dto.claimerAccountDto().branch(), dto.claimerDto().taxIdNumber()
        );
        if (claimer.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "Claimer not found.");
            return Either.left(notification);
        }
        Optional<Account> acc = claimer.get().getAccounts().stream()
                .filter(it -> it.getNumber().getNumber().equals(dto.claimerAccountDto().accountNumber()))
                .findAny();
        if (acc.isEmpty()) {
            return Either.left(Notification.create("Not found", 404, "Claimer account not found."));
        }
        Optional<EntryKey> key = acc.get().getEntryKeys().stream()
                .filter(it -> it.getKey().getKey().equals(dto.key())).findAny();
        if (key.isEmpty()) {
            return Either.left(Notification.create("Not found", 404, "Key not found."));
        }
        var donor = ownerRepository.findByKey(dto.key());
        if (donor.isEmpty()) {
            Notification notification = Notification.create("Not found", 404, "Donor not found");
            return Either.left(notification);
        }
        var notAllowed = ClaimService.validateTypeClaim(claimer.get(), donor.get(), dto.typeClaim(), acc.get().getParticipant().getParticipant());
        if (notAllowed == null) {
            var claim = ClaimService.create(
                    acc.get().getId().toString(),
                    acc.get().getParticipant().getParticipant(),
                    key.get().getKey().getKey(),
                    key.get().getKey().getType(),
                    donor.get().getAccounts().getFirst().getParticipant().getParticipant(),
                    dto.typeClaim(),
                    claimer.get().getTaxIdNumber().getTaxIdNumber(),
                    claimer.get().getType()
            );
            if (claim.getNotification().hasError()) {
                return Either.left(claim.getNotification());
            }
            claim = this.claimRepository.save(claim);
            var out = ClaimOutputDto.from(acc.get(), key.get(), claim, claimer.get());
            return Either.right(out);
        }
        return Either.left(notAllowed);
    }
}
