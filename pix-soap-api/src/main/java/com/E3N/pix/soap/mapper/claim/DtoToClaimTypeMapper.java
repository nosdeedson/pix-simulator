package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerDto;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

public class DtoToClaimTypeMapper {

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

    public static ClaimType toClaimResponse(ClaimOutputDto dto) {
        var claim = new ClaimType();
        claim.setClaimer(toOwnerType(dto.claimerDto()));
        claim.setCompletionPeriodEnd(DateHelper.fromInstant(dto.completionPeriodEnd()));
        claim.setDonorParticipant(dto.donorParticipant());
        claim.setId(dto.id());
        claim.setLastModified(DateHelper.fromInstant(dto.lastModified()));
        claim.setResolutionPeriodEnd(DateHelper.fromInstant(dto.resolutionPeriodEnd()));
        claim.setStatus(ClaimStatusType.valueOf(dto.statusClaim().name()));
        claim.setClaimerAccount(toAccountType(dto.claimerAccountDto()));
        claim.setKey(dto.key());
        claim.setKeyType(KeyType.valueOf(dto.typeKey().name()));
        claim.setType(TypeClaims.valueOf(dto.typeClaim().name()));
        return claim;
    }
}
