package com.E3N.pix.domain.mocks.domain;

import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.test.entrykey.RandomDate;
import com.E3N.test.entrykey.RandomKeys;
import com.github.javafaker.Faker;

public abstract class OwnerMock {

    private static final Faker faker = Faker.instance();

    public static Owner builder(
            final String keyOwnershipDate,
            final String name,
            final String tradeName,
            final String taxIdNumber,
            final TypePerson typePerson,
            final String[]... openClaimCreationDate
    ) {
        if (typePerson.equals(TypePerson.LEGAL_PERSON)) {
            return Owner.getInstanceLegalPerson(
                    keyOwnershipDate, name, taxIdNumber, tradeName, typePerson
            );
        } else {
            return Owner.getInstanceNaturalPerson(
                    keyOwnershipDate, name, taxIdNumber, typePerson
            );
        }
    }

    /**
     * document will be used to compare with the type of keys
     * if is a legal person or natural person the value of the key must be equal to the document
     *
     * @param typePerson
     * @param document
     * @return
     */
    public static Owner builder(final TypePerson typePerson, String document) {
        if (typePerson.equals(TypePerson.NATURAL_PERSON)) {
            var fullName = faker.name().fullName().replaceAll("[^a-zA-Z0-9\\s]", "");
            var doc = document != null ? document : RandomKeys.randomNaturalPersonDocument();
            return Owner.getInstanceNaturalPerson(
                    RandomDate.getRandomDate(),
                    fullName,
                    doc,
                    TypePerson.NATURAL_PERSON
            );
        }
        var company = faker.company().name().replaceAll("[^a-zA-Z0-9\\s]", "");
        var keyOwnershipDate = RandomDate.getRandomDate();
        document = document != null ? document : RandomKeys.randomLegalPersonDocument();
        return Owner.getInstanceLegalPerson(
                keyOwnershipDate,
                company,
                document,
                company,
                TypePerson.LEGAL_PERSON
        );
    }

    public static Owner getInvalidLegalPerson() {
        return Owner.getInstanceLegalPerson(
                "35/08/1641",
                "na",
                "123456789",
                "na",
                TypePerson.LEGAL_PERSON
        );
    }

}
