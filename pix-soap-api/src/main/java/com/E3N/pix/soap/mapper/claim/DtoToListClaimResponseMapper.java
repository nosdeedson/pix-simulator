package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerDto;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class DtoToListClaimResponseMapper {

    private static OwnerType getClaimer(ClaimerDto dto) {
        var claimer = new OwnerType();
        claimer.setType(OwnerTypeEnum.valueOf(dto.typePerson().name()));
        claimer.setTaxIdNumber(dto.taxIdNumber());
        claimer.setName(dto.name());
        claimer.setTradeName(dto.tradeName());
        return claimer;
    }

    private static AccountType getClaimerAccount(ClaimerAccountDto dto) {
        var acc = new AccountType();
        acc.setOpeningDate(DateHelper.fromString(dto.openingDate()));
        acc.setParticipant(dto.participant());
        acc.setBranch(dto.branch());
        acc.setAccountType(AccountTypeEnum.valueOf(dto.accountType().name()));
        acc.setAccountNumber(dto.accountNumber());
        return acc;
    }

    private static ClaimType getClaimType(ClaimOutputDto dto) {
        ClaimType ct = new ClaimType();
        ct.setType(TypeClaims.valueOf(dto.typeClaim().name()));
        ct.setKeyType(KeyType.valueOf(dto.typeKey().name()));
        ct.setKey(dto.key());
        ct.setClaimer(getClaimer(dto.claimerDto()));
        ct.setClaimerAccount(getClaimerAccount(dto.claimerAccountDto()));
        ct.setId(dto.id());
        ct.setStatus(ClaimStatusType.valueOf(dto.statusClaim().name()));
        ct.setResolutionPeriodEnd(DateHelper.fromInstant(dto.resolutionPeriodEnd()));
        ct.setLastModified(DateHelper.fromInstant(dto.lastModified()));
        ct.setCompletionPeriodEnd(DateHelper.fromInstant(dto.resolutionPeriodEnd()));
        ct.setDonorParticipant(dto.donorParticipant());
        ct.setType(TypeClaims.valueOf(dto.typeClaim().name()));
        return ct;
    }

    public static ListClaimsResponse getResponse(List<ClaimOutputDto> list) {
        var response = new ListClaimsResponse();
        response.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        response.setHasMoreElements(true);
        response.setResponseTime(DateHelper.fromInstant(Instant.now()));
        response.setSignature(UUID.randomUUID().toString());
        if (list.isEmpty()) {
            ClaimsType claimsType = new ClaimsType();
            claimsType.getClaim();
            response.setClaims(claimsType);

        } else {
            response.setClaims(new ClaimsType());
            for (ClaimOutputDto dto : list) {
                ClaimType ct = getClaimType(dto);
                response.getClaims().getClaim().add(ct);
            }
        }
        return response;
    }

    public static GetClaimResponse getClaimResponse(ClaimOutputDto dto){
        var response = new GetClaimResponse();
        response.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        response.setResponseTime(DateHelper.fromInstant(Instant.now()));
        response.setSignature(UUID.randomUUID().toString());
        response.setClaim(getClaimType(dto));
        return response;
    }

    public static AcknowledgeClaimResponse getAcknowledge(ClaimOutputDto dto){
        var response = new AcknowledgeClaimResponse();
        response.setCorrelationId(UUID.randomUUID().toString());
        response.setResponseTime(DateHelper.fromInstant(Instant.now()));
        response.setSignature(UUID.randomUUID().toString());
        response.setClaim(getClaimType(dto));
        return response;
    }

}
