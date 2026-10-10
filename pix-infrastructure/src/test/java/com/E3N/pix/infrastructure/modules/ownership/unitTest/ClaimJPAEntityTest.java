package com.E3N.pix.infrastructure.modules.ownership.unitTest;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.UnitTest;
import com.E3N.pix.infrastructure.modules.claim.ClaimJPAEntity;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ClaimJPAEntityTest extends UnitTest {

    Claim claim = Claim.getInstance(
            UUID.randomUUID().toString(),
            RandomParticipant.getParticipant(),
            RandomParticipant.getParticipant(),
            RandomKeysMock.randomEmails(),
            RandomCNPJMock.getRandomCNPJ(),
            TypeClaim.PORTABILITY,
            TypeKey.EMAIL,
            TypePerson.LEGAL_PERSON
    );

    @Test
    void givenValidClaim_whenCalling_from_shouldReturnClaimJPA(){
        var result = ClaimJPAEntity.from(claim);
        Assertions.assertInstanceOf(ClaimJPAEntity.class, result);
        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getResolutionPeriodEnd());
        Assertions.assertNotNull(result.getCompletionPeriodEnd());
    }

    @Test
    void givenValidClaimJPA_whenCalling_from_shouldReturnClaim(){
        ClaimJPAEntity claimJPA = ClaimJPAEntity.from(claim);
        var result = ClaimJPAEntity.from(claimJPA);
        Assertions.assertInstanceOf(Claim.class, result);
    }
}
