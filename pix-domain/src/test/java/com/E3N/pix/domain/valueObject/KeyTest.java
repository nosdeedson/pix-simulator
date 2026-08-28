package com.E3N.pix.domain.valueObject;

import com.E3N.pix.domain.UnitTest;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class KeyTest extends UnitTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenAValidTypeOfKey_shouldReturnAKeys(final String value, final TypeKey type) {
        var k = Key.getInstance(value, type);
        Assertions.assertInstanceOf(Key.class, k);
        Assertions.assertEquals(type, k.getType());
        Assertions.assertEquals(value, k.getKey());
        Assertions.assertNull(k.getNotification());
    }

    @ParameterizedTest
    @MethodSource("invalidProvider")
    public void givenAInvalidTypeOfKey_shouldReturnAKeys(final String value, final TypeKey type) {
        var k = Key.getInstance(value, type);
        Assertions.assertInstanceOf(Key.class, k);
        Assertions.assertNull(k.getType());
        Assertions.assertNull(k.getKey());
        Assertions.assertInstanceOf(Notification.class, k.getNotification());
        Assertions.assertTrue(k.getNotification().hasError());
        var expectedMessage = value + " is invalid.";
        Assertions.assertEquals(k.getNotification().getViolations().getFirst().message(), expectedMessage);
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("06372974827", TypeKey.CPF),
                Arguments.of("84686187000119", TypeKey.CNPJ),
                Arguments.of("S46Y7RD0000125", TypeKey.CNPJ),
                Arguments.of("+5535998697235", TypeKey.PHONE),
                Arguments.of("3b4c5d6e-7f8a-49b0-81c2-d3e4f5a6b7c8", TypeKey.EVP),
                Arguments.of("carlos-oliveira@empresa.com.br", TypeKey.EMAIL)
        );
    }

    static Stream<Arguments> invalidProvider() {
        return Stream.of(
                Arguments.of("06372974837", TypeKey.CPF),
                Arguments.of("84686187010119", TypeKey.CNPJ),
                Arguments.of("S46Y7RD0050125", TypeKey.CNPJ),
                Arguments.of("+55359986872", TypeKey.PHONE),
                Arguments.of("3b4c5d6e-7f8a-49b0-81c2-", TypeKey.EVP),
                Arguments.of("carlos-oliveiraempresa.com.br", TypeKey.EMAIL)
        );
    }
}
