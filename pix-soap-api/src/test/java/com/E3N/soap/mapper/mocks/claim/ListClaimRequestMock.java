package com.E3N.soap.mapper.mocks.claim;

import com.E3N.pix.soap.contract.ClaimStatusType;
import com.E3N.pix.soap.contract.ListClaimsRequest;
import com.E3N.pix.soap.contract.TypeClaims;
import com.E3N.shared.utils.DateHelper;
import com.E3N.test.Owner.RandomDateMock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class ListClaimRequestMock {

    public static ListClaimsRequest getListClaimRequest(Boolean allTrue, String participant){
        ListClaimsRequest request = new ListClaimsRequest();
        request.setIncludeIndirectParticipants(Boolean.FALSE);
        request.setIsClaimer(allTrue);
        request.setIsDonor(allTrue);
        Instant day = Instant.now();
        request.setModifiedAfter(DateHelper.fromInstant(day.plus(5, ChronoUnit.DAYS)));
        request.setModifiedBefore(DateHelper.fromInstant(day.minus(5, ChronoUnit.DAYS)));
        request.setParticipant(participant);
        request.setStatus(ClaimStatusType.OPEN);
        request.setType(TypeClaims.PORTABILITY);
        return request;
    }

    public static ListClaimsRequest getListClaimRequestWithAllNull(String participant){
        ListClaimsRequest request = new ListClaimsRequest();
        request.setIncludeIndirectParticipants(Boolean.FALSE);
        request.setIsClaimer(null);
        request.setIsDonor(null);
        request.setModifiedAfter(null);
        request.setModifiedBefore(null);
        request.setParticipant(participant);
        request.setStatus(null);
        request.setType(null);
        return request;
    }
}
