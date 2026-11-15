package com.E3N.soap.mocks.claim;

import com.E3N.pix.service.claim.enums.ClaimOperationReason;
import com.E3N.pix.soap.contract.CancelClaimRequest;
import com.E3N.pix.soap.contract.ClaimOperationReasonType;

public class CancelClaimMock {

    public static CancelClaimRequest cancelClaimRequestMock(
            String claimId, ClaimOperationReason reason, String participantRequest
    ){
        var request = new CancelClaimRequest();
        request.setClaimId(claimId);
        request.setReason(ClaimOperationReasonType.valueOf(reason.name()));
        request.setParticipant(participantRequest);
        return request;
    }
}
