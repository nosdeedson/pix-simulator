package com.E3N.pix.domain.mocks.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.test.Owner.*;

import java.util.UUID;

public abstract class ClaimMock {

    public static Claim getValidClaim(
            TypeClaim typeClaim,
            TypeKey typeKey,
            TypePerson typePerson,
            String key,
            String taxIdNumber
    ){
        return Claim.getInstance(
                UUID.randomUUID().toString(),
                RandomParticipant.getParticipant(),
                RandomParticipant.getParticipant(),
                key,
                taxIdNumber,
                typeClaim,
                typeKey,
                typePerson
        );
    }
}
