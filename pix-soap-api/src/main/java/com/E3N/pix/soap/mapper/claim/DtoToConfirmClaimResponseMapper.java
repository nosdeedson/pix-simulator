package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.soap.contract.ConfirmClaimResponse;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;
import java.util.UUID;

public class DtoToConfirmClaimResponseMapper {

    public static ConfirmClaimResponse getConfirmResponse(ClaimOutputDto dto){
        var claimType = DtoToClaimTypeMapper.toClaimResponse(dto);
        var confirmResponse = new ConfirmClaimResponse();
        confirmResponse.setClaim(claimType);
        confirmResponse.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        confirmResponse.setResponseTime(DateHelper.fromInstant(Instant.now()));
        confirmResponse.setSignature(UUID.randomUUID().toString());
        return confirmResponse;
    }
}
