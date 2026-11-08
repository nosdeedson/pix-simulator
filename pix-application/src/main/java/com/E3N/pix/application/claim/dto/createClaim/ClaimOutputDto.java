package com.E3N.pix.application.claim.dto.createClaim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;

import java.time.Instant;
import java.util.UUID;

public record ClaimOutputDto(
        Instant responseTime,
        String correlationId,
        TypeClaim typeClaim,
        String key,
        TypeKey typeKey,
        ClaimerAccountDto claimerAccountDto,
        ClaimerDto claimerDto,
        String donorParticipant,
        String id,
        StatusClaim statusClaim,
        Instant completionPeriodEnd,
        Instant resolutionPeriodEnd,
        Instant lastModified
) {

    public static ClaimOutputDto from(Claim claim, Owner claimer) {
        // TODO ADD HAS MORE CLAIMS FOR THE PARTICIPANT
        var account = claimer.getAccounts().stream()
                .filter(acc -> acc.getId().toString().equals(claim.getClaimerAccountId()))
                .findFirst().orElseThrow();
        var acc = new ClaimerAccountDto(account.getParticipant().getParticipant(), account.getBranch().getBranch(), account.getNumber().getNumber(), account.getType(), account.getOpeningDate().toString());
        var tradeName = claimer.getTradeName() == null ? null : claimer.getTradeName().getName();
        var claimerResponse = new ClaimerDto(claimer.getType(), claimer.getTaxIdNumber().getTaxIdNumber(), claimer.getName().getName(), tradeName);
        return new ClaimOutputDto(
                Instant.now(),
                UUID.randomUUID().toString().replace("-", ""),
                claim.getType(),
                claim.getKeyClaimed(),
                claim.getTypeKeyClaimed(),
                acc,
                claimerResponse,
                claim.getDonorParticipant(),
                claim.getId().toString(),
                claim.getStatus(),
                claim.getCompletionPeriodEnd(),
                claim.getResolutionPeriodEnd(),
                claim.getLastModified()
        );
    }
}
