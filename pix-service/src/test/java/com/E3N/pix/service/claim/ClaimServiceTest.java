package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

public class ClaimServiceTest extends UniTest {

    @InjectMocks
    private ClaimService claimService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    static List<Arguments> providerValidValues() {
        var keyForPortabilityCPF = RandomCpfMock.getRandomCFP();
        var keyForPortabilityCNPJ = RandomCNPJMock.getRandomCNPJ();
        return List.of(
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), RandomKeysMock.randomEmails(),
                        TypeKey.EMAIL, RandomParticipant.getParticipant(), TypeClaim.PORTABILITY, RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), RandomKeysMock.randomPhone(),
                        TypeKey.PHONE, RandomParticipant.getParticipant(), TypeClaim.PORTABILITY, RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), keyForPortabilityCPF,
                        TypeKey.CPF, RandomParticipant.getParticipant(), TypeClaim.PORTABILITY, keyForPortabilityCPF, TypePerson.NATURAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), keyForPortabilityCNPJ,
                        TypeKey.CNPJ, RandomParticipant.getParticipant(), TypeClaim.PORTABILITY, keyForPortabilityCNPJ, TypePerson.LEGAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), RandomKeysMock.randomPhone(),
                        TypeKey.PHONE, RandomParticipant.getParticipant(), TypeClaim.OWNERSHIP, keyForPortabilityCNPJ, TypePerson.LEGAL_PERSON)
        );
    }

    @ParameterizedTest
    @MethodSource("providerValidValues")
    void givenValidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim(
            String claimerAccountId,
            String claimerParticipant,
            String key,
            TypeKey typeKey,
            String donorParticipant,
            TypeClaim typeClaim,
            String claimerTaxIdNumber,
            TypePerson claimerTypePerson
    ) {
        var result = ClaimService.create(
                claimerAccountId,
                claimerParticipant,
                key,
                typeKey,
                donorParticipant,
                typeClaim,
                claimerTaxIdNumber,
                claimerTypePerson
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertFalse(result.getNotification().hasError());
    }

    static List<Arguments> providerEVPKeys() {
        return List.of(
                Arguments.of(RandomKeysMock.randomEVP(), TypeClaim.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomEVP(), TypeClaim.PORTABILITY)
        );
    }

    @ParameterizedTest
    @MethodSource("providerEVPKeys")
    void givenTypeKeyEVP_whenCalling_getInstance_shouldReturnClaimWithNotification(String key, TypeClaim typeClaim) {
        var result = ClaimService.create(
                UUID.randomUUID().toString(),
                RandomParticipant.getParticipant(),
                key,
                TypeKey.EVP,
                RandomParticipant.getParticipant(),
                typeClaim,
                RandomCNPJMock.getRandomCNPJ(),
                TypePerson.LEGAL_PERSON
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertEquals("Is not allowed to create a claim for a key if type is EVP", result.getNotification().getViolations().getFirst().reason());
    }

    static List<Arguments> providerInvalidValues() {
        var keyForPortabilityCPF = RandomCpfMock.getRandomCFP();
        var keyForPortabilityCNPJ = RandomCNPJMock.getRandomCNPJ();
        return List.of(
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), RandomKeysMock.randomEmails(),
                        TypeKey.EMAIL, RandomParticipant.getParticipant(), TypeClaim.OWNERSHIP, RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), keyForPortabilityCPF,
                        TypeKey.CPF, RandomParticipant.getParticipant(), TypeClaim.OWNERSHIP, keyForPortabilityCPF, TypePerson.NATURAL_PERSON),
                Arguments.of(UUID.randomUUID().toString(), RandomParticipant.getParticipant(), keyForPortabilityCNPJ,
                        TypeKey.CNPJ, RandomParticipant.getParticipant(), TypeClaim.OWNERSHIP, keyForPortabilityCNPJ, TypePerson.LEGAL_PERSON)
        );
    }

    @ParameterizedTest
    @MethodSource("providerInvalidValues")
    void givenInvalidValuesLegalPerson_whenCalling_getInstance_shouldReturnClaim(
            String claimerAccountId,
            String claimerParticipant,
            String key,
            TypeKey typeKey,
            String donorParticipant,
            TypeClaim typeClaim,
            String claimerTaxIdNumber,
            TypePerson claimerTypePerson
    ) {
        var result = ClaimService.create(
                claimerAccountId,
                claimerParticipant,
                key,
                typeKey,
                donorParticipant,
                typeClaim,
                claimerTaxIdNumber,
                claimerTypePerson
        );
        Assertions.assertInstanceOf(Claim.class, result);
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertEquals("The type of claim must be Portability", result.getNotification().getViolations().getFirst().reason());
    }

    @Test
    void givenRightTypeClaim_whenCalling_validateTypeClaim_shouldReturnNull() {
        var claimer = OwnerMock.mockOwner();
        var donor = OwnerMock.mockOwner();
        var claimerParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var result = ClaimService.validateTypeClaim(claimer, donor, TypeClaim.OWNERSHIP, claimerParticipant);
        Assertions.assertNull(result);
    }

    @Test
    void givenWrongTypeClaim_whenCalling_validateTypeClaim_shouldReturnNull() {
        var claimer = OwnerMock.mockOwner();
        var claimerParticipant = RandomParticipant.getParticipant();
        var result = ClaimService.validateTypeClaim(claimer, claimer, TypeClaim.PORTABILITY, claimerParticipant);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Type of claim must be Ownership", result.getDetail());
    }
}
