package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.ClaimerDto;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

import java.util.UUID;

public final class DtoToClaimResponseMapper {

    private static OwnerType toOwnerType(ClaimerDto dto) {
        var owner = new OwnerType();
        owner.setTradeName(dto.tradeName());
        owner.setType(OwnerTypeEnum.valueOf(dto.typePerson().name()));
        owner.setTaxIdNumber(dto.taxIdNumber());
        owner.setName(dto.name());
        return owner;
    }

    private static AccountType toAccountType(ClaimerAccountDto dto) {
        var acc = new AccountType();
        acc.setParticipant(dto.participant());
        acc.setOpeningDate(DateHelper.fromString(dto.openingDate()));
        acc.setBranch(dto.branch());
        acc.setAccountNumber(dto.accountNumber());
        acc.setAccountType(AccountTypeEnum.valueOf(dto.accountType().name()));
        return acc;
    }

    private static ClaimType toClaimResponse(ClaimOutputDto dto) {
        var claim = new ClaimType();
        claim.setClaimer(toOwnerType(dto.claimerDto()));
        claim.setClaimerAccount(toAccountType(dto.claimerAccountDto()));
        claim.setKey(dto.key());
        claim.setKeyType(KeyType.valueOf(dto.typeKey().name()));
        claim.setType(TypeClaims.valueOf(dto.typeClaim().name()));
        return claim;
    }

    public static CreateClaimResponse toResponse(ClaimOutputDto dto) {
        var response = new CreateClaimResponse();
        response.setCompletionPeriodEnd(DateHelper.fromInstant(dto.completionPeriodEnd()));
        response.setClaim(toClaimResponse(dto));
        response.setCorrelationId(UUID.randomUUID().toString());
        response.setDonorParticipant(dto.donorParticipant());
        response.setId(dto.id());
        response.setLastModified(DateHelper.fromInstant(dto.lastModified()));
        response.setResolutionPeriodEnd(DateHelper.fromInstant(dto.resolutionPeriodEnd()));
        response.setResponseTime(DateHelper.fromInstant(dto.responseTime()));
        response.setSignature(UUID.randomUUID().toString());
        response.setStatus(ClaimStatusType.valueOf(dto.statusClaim().name()));
        return response;
    }
}
