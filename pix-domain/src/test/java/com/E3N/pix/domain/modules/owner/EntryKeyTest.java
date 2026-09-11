package com.E3N.pix.domain.modules.owner;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

public class EntryKeyTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidValues_shouldReturnEntry_key(String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertEquals(result.getKey().getKey(), key);
        Assertions.assertEquals(result.getRequestId().toString(), requestId);
    }

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

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenInvalidValues_shouldReturnEntry_key(String key, TypeKey type, String requestId) {
        var result = EntryKey.getInstance(key, type, Reason.USER_REQUESTED, requestId);
        Assertions.assertInstanceOf(EntryKey.class, result);
        Assertions.assertInstanceOf(Notification.class, result.getNotification());
        Assertions.assertTrue(result.getNotification().hasError());
        Assertions.assertFalse(result.getNotification().getViolations().isEmpty());
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
}
