package com.E3N.pix.service.owner.mocks.dto;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomInvalidNameMock;
import com.github.javafaker.Faker;

public abstract class OwnerDtoMock {
    private static final Faker faker = new Faker();

    public static OwnerDto getOwner(TypePerson typePerson, TypeKey typeKey) {
        var name = "";
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            name = faker.company().name().replaceAll("[^a-zA-Z0-9\\-_]", "");

            return new OwnerDto.Builder()
                    .name(name)
                    .tradeName(name)
                    .taxIdNumber(RandomCNPJMock.getRandomCNPJ())
                    .account(AccountDtoMock.mockAccountDto(typeKey))
                    .typePerson(typePerson)
                    .build();
        }
        name = faker.name().fullName().replaceAll("[^a-zA-Z ]", "");
        return new OwnerDto.Builder()
                .name(name)
                .taxIdNumber(RandomCpfMock.getRandomCFP())
                .account(AccountDtoMock.mockAccountDto(typeKey))
                .typePerson(typePerson)
                .build();
    }

    public static OwnerDto getInvalidOwner(TypePerson typePerson, TypeKey typeKey) {
        var name = "";
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            return new OwnerDto.Builder()
                    .name(RandomInvalidNameMock.getInvalidCompanyName())
                    .tradeName(RandomInvalidNameMock.getInvalidCompanyName())
                    .taxIdNumber(RandomCNPJMock.getRandomInvalidCNPJ(null))
                    .account(AccountDtoMock.mockAccountDto(typeKey))
                    .typePerson(typePerson)
                    .build();
        }
        name = faker.name().fullName().replaceAll("[^a-zA-Z ]", "");
        return new OwnerDto.Builder()
                .name(RandomInvalidNameMock.getInvalidName())
                .taxIdNumber(RandomCpfMock.getRandomInvalidCPF())
                .account(AccountDtoMock.mockAccountDto(typeKey))
                .typePerson(typePerson)
                .build();
    }

}
