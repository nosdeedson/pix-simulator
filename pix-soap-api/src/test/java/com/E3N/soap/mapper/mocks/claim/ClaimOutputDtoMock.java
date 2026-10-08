package com.E3N.soap.mapper.mocks.claim;

import com.E3N.pix.application.claim.dto.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.ClaimerDto;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import com.github.javafaker.Faker;

import java.time.Instant;
import java.util.UUID;

public final class ClaimOutputDtoMock {

    static final Faker faker = new Faker();

    private static ClaimerAccountDto getClaimerAccountDto() {
        return new ClaimerAccountDto(
                RandomParticipant.getParticipant(),
                RandomAccountMock.randomBranch(),
                RandomAccountMock.randomAccountNumber(),
                AccountType.CACC,
                Instant.now().toString()
        );
    }

    private static ClaimerDto getClaimer() {
        var name = faker.company().name();
        return new ClaimerDto(
                TypePerson.LEGAL_PERSON,
                RandomCNPJMock.getRandomCNPJ(),
                name,
                name
        );
    }

    public static ClaimOutputDto mockClaimOutputDto() {
        return new ClaimOutputDto(
                Instant.now(),
                UUID.randomUUID().toString().replace("-", ""),
                TypeClaim.PORTABILITY,
                RandomKeysMock.randomPhone(),
                TypeKey.PHONE,
                getClaimerAccountDto(),
                getClaimer(),
                RandomParticipant.getParticipant(),
                UUID.randomUUID().toString(),
                StatusClaim.OPEN,
                Instant.now(),
                Instant.now(),
                Instant.now()

        );
    }
}
