package com.E3N.pix.application.claim.dto;

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

    public static ClaimOutputDto from(
            Account account, EntryKey key, Claim claim, Owner claimer
    ) {
        var acc = new ClaimerAccountDto(account.getParticipant().getParticipant(), account.getBranch().getBranch(), account.getNumber().getNumber(), account.getType(), account.getOpeningDate().toString());
        var tradeName = claimer.getTradeName() == null ? null : claimer.getTradeName().getName();
        var claimerResponse = new ClaimerDto(claimer.getType(), claimer.getTaxIdNumber().getTaxIdNumber(), claimer.getName().getName(), tradeName);
        return new ClaimOutputDto(
                Instant.now(),
                UUID.randomUUID().toString().replace("-", ""),
                claim.getType(),
                key.getKey().getKey(),
                key.getKey().getType(),
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
