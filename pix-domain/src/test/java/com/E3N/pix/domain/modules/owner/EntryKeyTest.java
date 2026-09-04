package com.E3N.pix.domain.modules.owner;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                Arguments.of(
                        RandomKeysMock.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomEVP(),
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomEmails(),
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomPhone(),
                        TypeKey.PHONE,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomEVP(),
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomLegalPersonDocument(),
                        TypeKey.CNPJ,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomNaturalPersonDocument(),
                        TypeKey.CPF,
                        UUID.randomUUID().toString()
                )
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
                Arguments.of(
                        "invalid-key",
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        RandomKeysMock.randomEVP(),
                        TypeKey.EVP,
                        "invalid-request-id"
                ),
                Arguments.of(
                        "Invalid",
                        TypeKey.EMAIL,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        "123456789456123",
                        TypeKey.CNPJ,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        "invalid-key",
                        TypeKey.EVP,
                        UUID.randomUUID().toString()
                ),
                Arguments.of(
                        "12345678912",
                        TypeKey.CPF,
                        UUID.randomUUID().toString()
                )
        );
    }
}
