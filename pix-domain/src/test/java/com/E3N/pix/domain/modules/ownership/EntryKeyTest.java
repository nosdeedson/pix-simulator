package com.E3N.pix.domain.modules.ownership;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.mocks.EntryKeyMock;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class EntryKeyTest extends UnitTest {

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(RandomKeysMock.randomEmails(), TypeKey.EMAIL, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomPhone(), TypeKey.PHONE, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomEVP(), TypeKey.EVP, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomEmails(), TypeKey.EMAIL, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomPhone(), TypeKey.PHONE, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomEVP(), TypeKey.EVP, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomLegalPersonDocument(), TypeKey.CNPJ, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomNaturalPersonDocument(), TypeKey.CPF, UUID.randomUUID().toString())
        );
    }

    static Stream<Arguments> invalidProvider() {
        return Stream.of(
                Arguments.of("invalid-key", TypeKey.EVP, UUID.randomUUID().toString()),
                Arguments.of(RandomKeysMock.randomEVP(), TypeKey.EVP, "invalid-request-id"),
                Arguments.of("Invalid", TypeKey.EMAIL, UUID.randomUUID().toString()),
                Arguments.of("123456789456123", TypeKey.CNPJ, UUID.randomUUID().toString()),
                Arguments.of("invalid-key", TypeKey.EVP, UUID.randomUUID().toString()),
                Arguments.of("12345678912", TypeKey.CPF, UUID.randomUUID().toString())
        );
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidValues_shouldReturnEntry_key(String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertEquals(result.getKey().getKey(), key);
        Assertions.assertEquals(result.getRequestId().toString(), requestId);
    }

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenInvalidValues_shouldReturnEntry_key(String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertFalse(result.getNotification().getViolations().isEmpty());
    }

    @Test
    public void givenValidValues_whenCallingGetInstanceWithId_shouldReturnSameId() {
        var expectedId = UUID.randomUUID();
        var expectedDate = Instant.parse(RandomDateMock.getRandomStringDate());
        var expectedRequestId = UUID.randomUUID().toString();
        var expectedCorrelationId = UUID.randomUUID().toString().replace("-", "");
        var expectedResponseTime = Instant.parse(RandomDateMock.getRandomStringDate());
        var expectedKeyOwnershipDate = Instant.parse(RandomDateMock.getRandomStringDate());
        var entryKey = EntryKey.getInstance(expectedId, expectedDate, expectedDate, null, RandomKeysMock.randomEmails(), TypeKey.EMAIL,
                expectedDate, Reason.USER_REQUESTED, expectedRequestId, expectedCorrelationId, expectedResponseTime, expectedKeyOwnershipDate);
        Assertions.assertInstanceOf(EntryKey.class, entryKey);
        Assertions.assertEquals(expectedId, entryKey.getId());
        Assertions.assertEquals(expectedDate, entryKey.getCreationDate());
        Assertions.assertEquals(expectedDate, entryKey.getUpdatedAt());
        Assertions.assertNull(entryKey.getDeletedAt());
        Assertions.assertEquals(expectedDate, entryKey.getCreationDate());
        Assertions.assertEquals(expectedCorrelationId, entryKey.getCorrelationId());
        Assertions.assertEquals(expectedRequestId, entryKey.getRequestId().toString());
        Assertions.assertEquals(expectedResponseTime, entryKey.getResponseTime());
        Assertions.assertEquals(expectedKeyOwnershipDate, entryKey.getKeyOwnershipDate());
    }

    @Test
    void givenSameValueOfEntryKey_whenCalling_isEqual_shouldReturnTrue() {
        var key = EntryKeyMock.getEntryKeyEmail();
        var expectedKey = key.getKey().getKey();
        Assertions.assertTrue(key.isEqual(expectedKey));
    }

    @Test
    void givenDifferentValueOfAnEntryKey_whenCalling_isEqual_shouldReturnFalse() {
        var key = EntryKeyMock.getEntryKeyEmail();
        var expectedKey = "different-one";
        Assertions.assertFalse(key.isEqual(expectedKey));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "BRANCH_TRANSFER",
                    "RFB_VALIDATION",
                    "RECONCILIATION",
            }
    )
    void givenRightReasonOfEntryKey_whenCalling_isInvalidUpdate_shouldReturnFalse(String stringReason) {
        var key = EntryKeyMock.getEntryKeyEVP();
        var expectedReason = Reason.valueOf(stringReason);
        Assertions.assertFalse(key.isInvalidUpdate(expectedReason));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "ACCOUNT_CLOSURE",
                    "FRAUD",
                    "PARTICIPANT_EXCLUSION",
                    "USER_REQUESTED",
            }
    )
    void givenWrongReasonOfEntryKey_whenCalling_isInvalidUpdate_shouldReturnFalse(String stringReason) {
        var key = EntryKeyMock.getEntryKeyEVP();
        var expectedReason = Reason.valueOf(stringReason);
        Assertions.assertTrue(key.isInvalidUpdate(expectedReason));
    }

    @ParameterizedTest
    @MethodSource("providerForValidUpdate")
    void givenRightReasonOfEntryKeyDifferentOfEVP_whenCalling_isInvalidUpdate_shouldReturnFalse(String stringReason, EntryKey key) {
        var expectedReason = Reason.valueOf(stringReason);
        Assertions.assertFalse(key.isInvalidUpdate(expectedReason));
    }

    static List<Arguments> providerForValidUpdate() {
        return List.of(
                Arguments.of("RFB_VALIDATION", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("RECONCILIATION", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("USER_REQUESTED", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("BRANCH_TRANSFER", EntryKeyMock.getEntryKeyCnpj()),

                Arguments.of("RFB_VALIDATION", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("RECONCILIATION", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("USER_REQUESTED", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("BRANCH_TRANSFER", EntryKeyMock.getEntryKeyCpf()),

                Arguments.of("RFB_VALIDATION", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("RECONCILIATION", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("USER_REQUESTED", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("BRANCH_TRANSFER", EntryKeyMock.getEntryKeyEmail()),

                Arguments.of("RFB_VALIDATION", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("RECONCILIATION", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("USER_REQUESTED", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("BRANCH_TRANSFER", EntryKeyMock.getEntryKeyPhone())
        );
    }

    @ParameterizedTest
    @MethodSource("providerForInvalidUpdate")
    void givenWrongReasonOfEntryKeyDifferentOfEVP_whenCalling_isInvalidUpdate_shouldReturnFalse(String stringReason, EntryKey key) {
        var expectedReason = Reason.valueOf(stringReason);
        Assertions.assertTrue(key.isInvalidUpdate(expectedReason));
    }

    static List<Arguments> providerForInvalidUpdate() {
        return List.of(
                Arguments.of("ACCOUNT_CLOSURE", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("FRAUD", EntryKeyMock.getEntryKeyCnpj()),
                Arguments.of("PARTICIPANT_EXCLUSION", EntryKeyMock.getEntryKeyCnpj()),

                Arguments.of("ACCOUNT_CLOSURE", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("FRAUD", EntryKeyMock.getEntryKeyCpf()),
                Arguments.of("PARTICIPANT_EXCLUSION", EntryKeyMock.getEntryKeyCpf()),

                Arguments.of("ACCOUNT_CLOSURE", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("FRAUD", EntryKeyMock.getEntryKeyEmail()),
                Arguments.of("PARTICIPANT_EXCLUSION", EntryKeyMock.getEntryKeyEmail()),

                Arguments.of("ACCOUNT_CLOSURE", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("FRAUD", EntryKeyMock.getEntryKeyPhone()),
                Arguments.of("PARTICIPANT_EXCLUSION", EntryKeyMock.getEntryKeyPhone())
        );
    }
}
