package com.E3N.pix.soap.mapper.claim;


import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerDto;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

import java.util.UUID;

public final class DtoToCreateClaimResponseMapper {

    public static CreateClaimResponse toResponse(ClaimOutputDto dto) {
        var response = new CreateClaimResponse();
        response.setClaim(DtoToClaimTypeMapper.toClaimResponse(dto));
        response.setCorrelationId(UUID.randomUUID().toString());
        response.setResponseTime(DateHelper.fromInstant(dto.responseTime()));
        response.setSignature(UUID.randomUUID().toString());
        return response;
    }
}
