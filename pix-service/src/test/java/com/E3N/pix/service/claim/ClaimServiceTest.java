package com.E3N.pix.service.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.claim.enums.ClaimOperationReason;
import com.E3N.pix.service.mocks.ClaimServiceMock;
import com.E3N.pix.service.ownership.mocks.OwnerMock;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class ClaimServiceTest extends UniTest {

    static final Faker faker = new Faker();

    @InjectMocks
    private ClaimService claimService;

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

    static List<Arguments> providerEVPKeys() {
        return List.of(
                Arguments.of(RandomKeysMock.randomEVP(), TypeClaim.OWNERSHIP),
                Arguments.of(RandomKeysMock.randomEVP(), TypeClaim.PORTABILITY)
        );
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset();
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
        var key = EntryKey.getInstance(RandomKeysMock.randomEVP(), TypeKey.EVP, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var claimer = OwnerMock.mockOwner(key);
        var donatingKey = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var donor = OwnerMock.mockOwner(donatingKey);
        var claimerParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var typeOfKey = donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getType();
        var result = ClaimService.validateTypeClaim(claimer, donor, TypeClaim.OWNERSHIP, claimerParticipant, typeOfKey);
        Assertions.assertNull(result);
    }

    @Test
    void givenWrongTypeClaim_whenCalling_validateTypeClaim_shouldReturnNull() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var claimer = OwnerMock.mockOwner(key);
        var result = ClaimService.validateTypeClaim(claimer, claimer, TypeClaim.PORTABILITY, RandomParticipant.getParticipant(), TypeKey.PHONE);
        Assertions.assertInstanceOf(Notification.class, result);
        Assertions.assertEquals("Type of claim must be Ownership", result.getDetail());
    }

    @Test
    void givenValidValues_whenCalling_getEntryToDonate_shouldReturnEntryKey() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var donor = OwnerMock.mockOwner(key);
        var expectedKey = donor.getAccounts().getFirst().getEntryKeys().getFirst().getKey().getKey();
        var result = ClaimService.getEntryToDonate(donor, expectedKey).orElseThrow();
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertEquals(expectedKey, result.getKey().getKey());
    }

    @Test
    void givenInvalidValues_whenCalling_getEntryToDonate_shouldReturnEmptyOptional() {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var donor = OwnerMock.mockOwner(key);
        var expectedKey = "any-key";
        var result = ClaimService.getEntryToDonate(donor, expectedKey);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void givenValidValues_whenCalling_confirmClaim_shouldThrow() {
        Instant fiveDaysInFuture = Instant.now().plus(5, ChronoUnit.DAYS);
        try (MockedStatic<Instant> mockedInstant =
                     Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedInstant.when(Instant::now).thenReturn(fiveDaysInFuture);
            var donatingKey = EntryKey.getInstance(RandomKeysMock.randomPhone(), TypeKey.PHONE, Reason.USER_REQUESTED, UUID.randomUUID().toString());
            var donor = OwnerMock.mockOwner(donatingKey);
            var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
            var claimer = OwnerMock.mockOwner(key);
            var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
            var service = new ClaimService();
            var result = Assertions.assertThrows(RuntimeException.class, () -> service.confirmClaim(
                    claim, donor, claimer,
                    donor.getAccounts().getFirst().getParticipant().getParticipant(), ClaimOperationReason.DEFAULT_OPERATION
            ));

            Assertions.assertEquals("This operation is not allowed, wait until the resolution period finishes", result.getMessage());
        }
    }

    @Test
    void givenValidValuesToConfirmByDefault_whenCalling_confirmClaim_shouldRemoveKeyFromDonor(){
        Instant today = Instant.now();
        Instant eightDayAgo = today.minus(8, ChronoUnit.DAYS);
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(Instant::now).thenReturn(eightDayAgo);
            var donatingKey = EntryKey.getInstance(RandomKeysMock.randomPhone(), TypeKey.PHONE, Reason.USER_REQUESTED, UUID.randomUUID().toString());
            var donor = OwnerMock.mockOwner(donatingKey);
            var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
            var claimer = OwnerMock.mockOwner(key);
            var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
            mockedStatic.when(Instant::now).thenReturn(today);
            var service = new ClaimService();
            var claimerParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
            Assertions.assertDoesNotThrow(() -> service.confirmClaim(claim, donor, claimer, claimerParticipant, ClaimOperationReason.DEFAULT_OPERATION));
            Assertions.assertTrue(donor.getAccounts().isEmpty());
        }
    }

    @Test
    void givenUserRequestToConfirmClaim_whenCalling_confirmClaim_shouldTransferKey(){
        var donatingKey = EntryKey.getInstance(RandomKeysMock.randomPhone(), TypeKey.PHONE, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var donor = OwnerMock.mockOwner(donatingKey);
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var claimer = OwnerMock.mockOwner(key);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        var claimerParticipant = claimer.getAccounts().getFirst().getParticipant().getParticipant();
        var service = new ClaimService();
        Assertions.assertDoesNotThrow(() -> service.confirmClaim(claim, donor, claimer, claimerParticipant, ClaimOperationReason.USER_REQUESTED));
        Assertions.assertTrue(donor.getAccounts().isEmpty());
        Assertions.assertEquals(2, claimer.getAccounts().getFirst().getEntryKeys().size());
    }

    @Test
    void givenInvalidValues_whenCalling_confirmClaim_shouldThrow(){
        var donatingKey = EntryKey.getInstance(RandomKeysMock.randomPhone(), TypeKey.PHONE, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var donor = OwnerMock.mockOwner(donatingKey);
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var claimer = OwnerMock.mockOwner(key);
        var claim = ClaimServiceMock.mockClaim(claimer, donor, TypeClaim.OWNERSHIP);
        var claimerParticipant = donor.getAccounts().getFirst().getParticipant().getParticipant();
        var service = new ClaimService();
        var result = Assertions.assertThrows(RuntimeException.class, () -> service.confirmClaim(claim, donor, claimer, claimerParticipant, ClaimOperationReason.USER_REQUESTED));
        Assertions.assertEquals("The operation does not match any requirement.", result.getMessage());
    }
}
