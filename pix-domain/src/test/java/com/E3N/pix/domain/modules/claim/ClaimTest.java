package com.E3N.pix.domain.modules.claim;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.mocks.claim.ClaimMock;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Violation;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;


public class ClaimTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("providerValidValues")
    void givenValidValidValues_whenCalling_getInstance_shouldReturnClaim(
            TypeClaim typeClaim, TypeKey typeKey, TypePerson typePerson, String key, String taxIdNumber
    ){
        var claim = ClaimMock.getValidClaim(
                typeClaim,
                typeKey,
                typePerson,
                key,
                taxIdNumber
        );
        Assertions.assertInstanceOf(Claim.class, claim);
        Assertions.assertFalse(claim.getNotification().hasError());
    }

    static List<Arguments> providerValidValues(){
        return List.of(
                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.CPF, TypePerson.NATURAL_PERSON, "88756715838", "88756715838"),
                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.EMAIL, TypePerson.NATURAL_PERSON, RandomKeysMock.randomEmails(), "88756715838"),
                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.PHONE, TypePerson.NATURAL_PERSON, RandomKeysMock.randomPhone(), "88756715838"),

                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.CNPJ, TypePerson.LEGAL_PERSON, "41977322000172", "41977322000172"),
                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.EMAIL, TypePerson.LEGAL_PERSON, RandomKeysMock.randomEmails(), "41977322000172"),
                Arguments.arguments(TypeClaim.PORTABILITY, TypeKey.PHONE, TypePerson.LEGAL_PERSON, RandomKeysMock.randomPhone(), "41977322000172")
        );
    }

    @ParameterizedTest
    @MethodSource("providerInvalidValues")
    void givenValidValidValues_whenCalling_getInstance_shouldReturnClaimWithNotification(
            TypeClaim typeClaim, TypeKey typeKey, TypePerson typePerson, String key, String taxIdNumber
    ){
        var claim = ClaimMock.getValidClaim(
                typeClaim,
                typeKey,
                typePerson,
                key,
                taxIdNumber
        );
        var expectedErrors = List.of(
                "TaxIdNumber is invalid.",
                "Claim must be opened as OWNERSHIP",
                "Is not allowed to create a claim for a key if type is EVP",
                "This kind of Key must open a portability",
                "This kind of portability need to have key equals to taxIdNumber",
                "The type of key must be CPF"
        );
        Assertions.assertInstanceOf(Claim.class, claim);
        Assertions.assertTrue(claim.getNotification().hasError());
        Assertions.assertTrue(expectedErrors.contains(claim.getNotification().getViolations().getFirst().reason() ));
    }

    static List<Arguments> providerInvalidValues(){
        return List.of(
                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.CPF, TypePerson.NATURAL_PERSON, "88756715838", "32632502306"),
                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.EVP, TypePerson.NATURAL_PERSON, RandomKeysMock.randomEVP(), "88756715838"),
                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.EMAIL, TypePerson.NATURAL_PERSON, RandomKeysMock.randomPhone(), "11111111111"),

                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.CNPJ, TypePerson.LEGAL_PERSON, "41977322000172", "25597632000105"),
                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.EVP, TypePerson.LEGAL_PERSON, RandomKeysMock.randomEmails(), "41977322000172"),
                Arguments.arguments(TypeClaim.OWNERSHIP, TypeKey.EVP, TypePerson.LEGAL_PERSON, RandomKeysMock.randomEmails(), "11111111111111")
        );
    }
}
