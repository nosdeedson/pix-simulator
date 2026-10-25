package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.ListClaim.ListClaimDto;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.soap.contract.ClaimStatusType;
import com.E3N.pix.soap.contract.ListClaimsRequest;
import com.E3N.shared.utils.DateHelper;

public final class ListClaimRequestToDtoMapper {

    public static ListClaimDto from(ListClaimsRequest request) {
        int limit = request.getLimit() == null ? 20 : request.getLimit();
        StatusClaim status = null;
        if (request.getStatus() != null){
            status = StatusClaim.valueOf(request.getStatus().name());
        }
        TypeClaim type = null;
        if (request.getType() != null){
            type = TypeClaim.valueOf(request.getType().name());
        }
        return new ListClaimDto(
                request.getParticipant(),
                request.isIncludeIndirectParticipants(),
                request.isIsDonor(),
                request.isIsClaimer(),
                status,
                type,
                DateHelper.from(request.getModifiedAfter()),
                DateHelper.from(request.getModifiedBefore()),
                limit
        );
    }
}
