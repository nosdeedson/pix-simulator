package com.E3N.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.createClaim.ClaimOutputDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.createClaim.ClaimerDto;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.soap.contract.ListClaimsResponse;
import com.E3N.pix.soap.mapper.claim.DtoToListClaimResponseMapper;
import com.E3N.soap.UnitTest;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class DtoToListClaimResponseMapperTest extends UnitTest {

    Faker faker = new Faker();
    String fullName = faker.name().fullName();

    ClaimerAccountDto claimerAccountDto = new ClaimerAccountDto(
            RandomParticipant.getParticipant(),
            RandomAccountMock.randomBranch(),
            RandomAccountMock.randomAccountNumber(),
            AccountType.CACC,
            Instant.now().toString()
    );
    ClaimerDto claimerDto = new ClaimerDto(TypePerson.NATURAL_PERSON, RandomCpfMock.getRandomCFP(), fullName, null );

    List<ClaimOutputDto> list = List.of(
            new ClaimOutputDto(
                    Instant.now(),
                    UUID.randomUUID().toString().replace("-", ""),
                    TypeClaim.PORTABILITY,
                    RandomKeysMock.randomEmails(),
                    TypeKey.EMAIL,
                    claimerAccountDto,
                    claimerDto,
                    RandomParticipant.getParticipant(),
                    UUID.randomUUID().toString(),
                    StatusClaim.OPEN,
                    Instant.now().plus(5, ChronoUnit.DAYS),
                    Instant.now().plus(8, ChronoUnit.DAYS),
                    Instant.now()
            ),
            new ClaimOutputDto(
                    Instant.now(),
                    UUID.randomUUID().toString().replace("-", ""),
                    TypeClaim.OWNERSHIP,
                    RandomKeysMock.randomPhone(),
                    TypeKey.PHONE,
                    claimerAccountDto,
                    claimerDto,
                    RandomParticipant.getParticipant(),
                    UUID.randomUUID().toString(),
                    StatusClaim.OPEN,
                    Instant.now().plus(5, ChronoUnit.DAYS),
                    Instant.now().plus(8, ChronoUnit.DAYS),
                    Instant.now()
            ),
            new ClaimOutputDto(
                    Instant.now(),
                    UUID.randomUUID().toString().replace("-", ""),
                    TypeClaim.PORTABILITY,
                    RandomKeysMock.randomEVP(),
                    TypeKey.EVP,
                    claimerAccountDto,
                    claimerDto,
                    RandomParticipant.getParticipant(),
                    UUID.randomUUID().toString(),
                    StatusClaim.OPEN,
                    Instant.now().plus(5, ChronoUnit.DAYS),
                    Instant.now().plus(8, ChronoUnit.DAYS),
                    Instant.now()
            )

    );

    @Test
    void givenEmptyListOfClaims_whenCalling_mapper_shouldReturnResponse(){
        List<ClaimOutputDto> list1 = List.of();
        var result = DtoToListClaimResponseMapper.getResponse(list1);
        Assertions.assertInstanceOf(ListClaimsResponse.class, result);
        Assertions.assertTrue(result.getClaims().getClaim().isEmpty());
    }

    @Test
    void givenNotEmptyListOfClaims_whenCalling_mapper_shouldReturnResponse(){
        var result = DtoToListClaimResponseMapper.getResponse(list);
        Assertions.assertInstanceOf(ListClaimsResponse.class, result);
        Assertions.assertFalse(result.getClaims().getClaim().isEmpty());
        Assertions.assertEquals(3, result.getClaims().getClaim().size());
    }
}
