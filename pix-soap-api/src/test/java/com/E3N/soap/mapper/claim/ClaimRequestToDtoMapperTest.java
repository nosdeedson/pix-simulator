package com.E3N.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.ClaimDto;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.soap.contract.KeyType;
import com.E3N.pix.soap.contract.TypeClaims;
import com.E3N.pix.soap.mapper.claim.CreateClaimRequestToDtoMapper;
import com.E3N.soap.UnitTest;
import com.E3N.soap.mapper.mocks.claim.ClaimRequestMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

public class ClaimRequestToDtoMapperTest extends UnitTest {

    static List<Arguments> providerRequest() {
        return List.of(
                Arguments.of(RandomKeysMock.randomEVP(), KeyType.EVP, TypeClaims.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomEVP(), KeyType.EVP, TypeClaims.PORTABILITY),
                Arguments.of(RandomKeysMock.randomPhone(), KeyType.PHONE, TypeClaims.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomPhone(), KeyType.PHONE, TypeClaims.PORTABILITY),
                Arguments.of(RandomKeysMock.randomEmails(), KeyType.EMAIL, TypeClaims.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomEmails(), KeyType.EMAIL, TypeClaims.PORTABILITY),
                Arguments.of(RandomKeysMock.randomLegalPersonDocument(), KeyType.CNPJ, TypeClaims.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomLegalPersonDocument(), KeyType.CNPJ, TypeClaims.PORTABILITY),
                Arguments.of(RandomKeysMock.randomNaturalPersonDocument(), KeyType.CPF, TypeClaims.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomNaturalPersonDocument(), KeyType.CPF, TypeClaims.PORTABILITY)
        );
    }

    @ParameterizedTest
    @MethodSource("providerRequest")
    void givenACreateClaimRequest_whenCalling_fromClaimType_shouldReturnClaimDto(String key, KeyType keyType, TypeClaims typeClaims) {
        var dto = ClaimRequestMock.createClaimRequestMock(key, keyType, typeClaims);
        var result = CreateClaimRequestToDtoMapper.from(dto.getClaim());
        Assertions.assertInstanceOf(ClaimDto.class, result);
        Assertions.assertEquals(dto.getClaim().getClaimer().getTaxIdNumber(), result.claimerDto().taxIdNumber());
    }
}
