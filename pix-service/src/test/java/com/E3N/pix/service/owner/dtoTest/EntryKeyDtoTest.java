package com.E3N.pix.service.owner.dtoTest;

import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.owner.dto.EntryKeyDto;
import com.E3N.test.Owner.RandomKeysMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

public class EntryKeyDtoTest extends UniTest {

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidValues_shouldReturnEntryKeyDto(final String naturalPerson, final TypeKey typeKey) {
        var result = new EntryKeyDto(naturalPerson, typeKey, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        Assertions.assertInstanceOf(EntryKeyDto.class, result);
        Assertions.assertEquals(typeKey, result.typeKey());
        Assertions.assertEquals(naturalPerson, result.key());
    }

    @ParameterizedTest
    @MethodSource("provider")
    public void givenValidEntryKeysDto_shouldReturnEntryKeyEntity(final String naturalPerson, final TypeKey typeKey) {
        var dto = new EntryKeyDto(naturalPerson, typeKey, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var entity = dto.toEntity();
        Assertions.assertInstanceOf(EntryKey.class, entity);
    }

    private static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of(RandomKeysMock.randomNaturalPersonDocument(), TypeKey.CPF),
                Arguments.of(RandomKeysMock.randomLegalPersonDocument(), TypeKey.CNPJ),
                Arguments.of(UUID.randomUUID().toString(), TypeKey.EVP),
                Arguments.of(RandomKeysMock.randomPhone(), TypeKey.PHONE),
                Arguments.of(RandomKeysMock.randomEmails(), TypeKey.EMAIL)
        );
    }
}
