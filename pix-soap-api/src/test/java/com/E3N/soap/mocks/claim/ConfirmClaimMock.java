package com.E3N.soap.mocks.claim;

import com.E3N.pix.soap.contract.ClaimOperationReasonType;
import com.E3N.pix.soap.contract.ConfirmClaimRequest;
import com.E3N.test.Owner.RandomParticipant;

import java.util.UUID;

public class ConfirmClaimMock {

    public static ConfirmClaimRequest getRequest(String claimId, String participant, ClaimOperationReasonType reason){
        var request = new ConfirmClaimRequest();
        request.setClaimId(claimId);
        request.setParticipant(participant);
        request.setReason(reason);
        request.setSignature(UUID.randomUUID().toString());
        return request;
    }
}
