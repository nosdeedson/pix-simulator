package com.E3N.pix.service.owner.dtoTest;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.UniTest;
import com.E3N.pix.service.owner.dto.AccountDto;
import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.pix.service.owner.mocks.dto.AccountDtoMock;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class OwnerDtoTest extends UniTest {
    private static final Faker faker = new Faker();

    @ParameterizedTest
    @MethodSource("providerNaturalPerson")
    public void givenValidValues_shouldInstantiateOwnerDtoOfNaturalPerson(
            final String name,
            final String taxIdNumber,
            TypePerson typePerson,
            AccountDto dto
    ) {
        var result = new OwnerDto.Builder()
                .name(name)
                .taxIdNumber(taxIdNumber)
                .typePerson(typePerson)
                .account(dto)
                .build();
        Assertions.assertInstanceOf(OwnerDto.class, result);
        Assertions.assertEquals(name, result.name());
        Assertions.assertEquals(taxIdNumber, result.taxIdNumber());
        Assertions.assertEquals(typePerson, result.typePerson());
        Assertions.assertInstanceOf(AccountDto.class, result.account());
        Assertions.assertNull(result.tradeName());
        Assertions.assertNull(result.openClaimCreationDate());
    }

    static Stream<Arguments> providerNaturalPerson() {
        return Stream.of(
                Arguments.of(faker.name().lastName(), RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.EMAIL)),
                Arguments.of(faker.name().lastName(), RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.EVP)),
                Arguments.of(faker.name().lastName(), RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.PHONE)),
                Arguments.of(faker.name().lastName(), RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.CNPJ)),
                Arguments.of(faker.name().lastName(), RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.CPF)));
    }

    @ParameterizedTest
    @MethodSource("providerLegalPerson")
    public void givenValidValues_shouldInstantiateOwnerDtoOfLegalPerson(
            final String name,
            final String taxIdNumber,
            TypePerson typePerson,
            AccountDto dto
    ) {
        var result = new OwnerDto
                .Builder()
                .name(name)
                .tradeName(name)
                .taxIdNumber(taxIdNumber)
                .typePerson(typePerson)
                .account(dto)
                .build();
        Assertions.assertInstanceOf(OwnerDto.class, result);
        Assertions.assertEquals(name, result.name());
        Assertions.assertEquals(taxIdNumber, result.taxIdNumber());
        Assertions.assertEquals(typePerson, result.typePerson());
        Assertions.assertInstanceOf(AccountDto.class, result.account());
        Assertions.assertEquals(name, result.tradeName());
        Assertions.assertNull(result.openClaimCreationDate());
    }

    static Stream<Arguments> providerLegalPerson() {
        return Stream.of(
                Arguments.of(faker.company().name(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.EMAIL)),
                Arguments.of(faker.company().name(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.EVP)),
                Arguments.of(faker.company().name(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.PHONE)),
                Arguments.of(faker.company().name(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.CNPJ)),
                Arguments.of(faker.company().name(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON,
                        AccountDtoMock.mockAccountDto(TypeKey.CPF)));

    }
}
