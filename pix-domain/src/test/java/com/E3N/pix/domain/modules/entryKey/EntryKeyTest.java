package com.E3N.pix.domain.modules.entryKey;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.mocks.domain.AccountMock;
import com.E3N.pix.domain.mocks.domain.OwnerMock;
import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.entryKey.EntryKey;
import com.E3N.pix.domain.modules.entry.entryKey.Reason;
import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.Error;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.entrykey.RandomKeys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public class EntryKeyTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidValues_shouldReturnEntry_key(Account account, Owner owner, String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, account, owner, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Account.class, result.getAccount());
        Assertions.assertInstanceOf(Owner.class, result.getOwner());
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        RandomKeys.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        RandomKeys.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        RandomKeys.randomEVP(),
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        RandomKeys.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        RandomKeys.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        RandomKeys.randomEVP(),
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenInvalidValues_shouldReturnEntry_key(Account account, Owner owner, String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, account, owner, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertTrue(!result.getNotification().getViolations().isEmpty());
    }

    static Stream<Arguments> invalidProvider() {
        return Stream.of(
                Arguments.of(
                        AccountMock.getInvalidOne(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        RandomKeys.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.getInvalidLegalPerson(),
                        RandomKeys.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        "invalid-key",
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.LEGAL_PERSON, RandomKeys.randomLegalPersonDocument()),
                        RandomKeys.randomEVP(),
                        TypeKey.EVP,
                        "invalid-request-id"
                ),
                Arguments.of(
                        AccountMock.getInvalidOne(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        RandomKeys.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.getInvalidLegalPerson(),
                        RandomKeys.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        "invalid-key",
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        AccountMock.builder(),
                        OwnerMock.builder(TypePerson.NATURAL_PERSON, RandomKeys.randomNaturalPersonDocument()),
                        RandomKeys.randomEVP(),
                        TypeKey.EVP,
                        "invalid-request-id"
                )
        );
    }

    // entryKey type cnpj legal person
    @Test
    public void givenValidValuesForAnEntryKeyOfALegalPerson_shouldReturnEntryKey() {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, expectedKey);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Account.class, result.getAccount());
        Assertions.assertInstanceOf(Owner.class, result.getOwner());
    }

    @Test
    public void givenInvalidValuesForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification() {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, null);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().getViolations().getFirst().message().contains("Key R9WEGOGH000166 should be equal to taxIdNumber"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Invalid-request-id"})
    @NullSource
    public void givenInvalidRequestIdForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification(final String requestId) {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, null);

        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().getViolations().getFirst().message().contains("Request Id is invalid or null."));
    }

    @Test
    public void givenInvalidReasonForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification() {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, expectedKey);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, null, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().getViolations().getFirst().message().contains("Reason is required."));
    }

    @Test
    public void givenInvalidOwnerForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification() {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.getInvalidLegalPerson();

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertEquals(4, result.getNotification().getViolations().size());
        var expectedErrors = Arrays.asList(
                new Error("KeyOwnershipDate is required."),
                new Error("Min size of name is 3, Max size is 100."),
                new Error("Min size of name is 3, Max size is 100."),
                new Error("TaxIdNumber: 123456789 is invalid.")
        );
        Assertions.assertTrue(expectedErrors.containsAll(result.getNotification().getViolations()));
    }

    @Test
    public void givenInvalidAccountForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification() {
        var expectedKey = "R9WEGOGH000166";
        var account = AccountMock.getInvalidOne();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, expectedKey);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertEquals(5, result.getNotification().getViolations().size());
        var expectedErrors = Arrays.asList(
                new Error("Branch is invalid."),
                new Error("Account Number is invalid."),
                new Error("OpeningDate is required."),
                new Error("Participant is invalid."),
                new Error("Account Type is required.")
        );
        Assertions.assertTrue(expectedErrors.containsAll(result.getNotification().getViolations()));
    }

    @Test
    public void givenInvalidKeyForAnEntryKeyOfALegalPerson_shouldReturnEntryKeyWithNotification() {
        var expectedKey = "R9WEGO%H000166";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.LEGAL_PERSON, expectedKey);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CNPJ, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertEquals(2, result.getNotification().getViolations().size());
        var expectedErrors = Arrays.asList(
                new Error("R9WEGO%H000166 is invalid."),
                new Error("TaxIdNumber: R9WEGO%H000166 is invalid.")
        );
        Assertions.assertTrue(expectedErrors.containsAll(result.getNotification().getViolations()));
    }

    // entryKey type natural person
    @Test
    public void givenValidValuesForAnEntryKeyOfANaturalPerson_shouldReturnEntryKey() {
        var expectedKey = RandomKeys.randomNaturalPersonDocument();
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.NATURAL_PERSON, expectedKey);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CPF, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Account.class, result.getAccount());
        Assertions.assertInstanceOf(Owner.class, result.getOwner());
    }

    @Test
    public void givenInvalidValuesForAnEntryKeyOfANaturalPerson_shouldReturnEntryKey() {
        var expectedKey = "91621117448";
        var account = AccountMock.builder();
        var owner = OwnerMock.builder(TypePerson.NATURAL_PERSON, null);

        var expectedRequestId = UUID.randomUUID();
        var result = EntryKey.getInstance(expectedKey, TypeKey.CPF, account, owner, Reason.USER_REQUESTED, expectedRequestId.toString());
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().getViolations().getFirst().message().contains("Key 91621117448 should be equal"));
    }
}
