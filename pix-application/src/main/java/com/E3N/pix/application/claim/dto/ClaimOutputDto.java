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
        String responseTime,
        String correlationId,
        TypeClaim typeClaim,
        String key,
        TypeKey typeKey,
        ClaimerAccountDto claimerAccountDto,
        ClaimerDto claimerDto,
        String donorParticipant,
        String id,
        StatusClaim statusClaim,
        String completionPeriodEnd,
        String resolutionPeriodEnd,
        String lastModified
) {

    public static ClaimOutputDto from(
            Account account, EntryKey key, Claim claim, Owner owner
    ){
        var acc = new ClaimerAccountDto(account.getParticipant().getParticipant(), account.getBranch().getBranch(), account.getNumber().getNumber(), account.getType(), account.getOpeningDate().toString());
        var claimer = new ClaimerDto(owner.getType(), owner.getTaxIdNumber().getTaxIdNumber(), owner.getName().getName(), owner.getTradeName().getName());
        return new ClaimOutputDto(
                Instant.now().toString(),
                UUID.randomUUID().toString().replace("-" , ""),
                claim.getType(),
                key.getKey().getKey(),
                key.getKey().getType(),
                acc,
                claimer,
                claim.getDonorParticipant(),
                claim.getId().toString(),
                claim.getStatus(),
                claim.getCompletionPeriodEnd().toString(),
                claim.getResolutionPeriodEnd().toString(),
                claim.getLastModified().toString()
        );
    }
}
