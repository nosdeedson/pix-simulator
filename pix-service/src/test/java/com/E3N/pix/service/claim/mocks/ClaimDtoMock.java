package com.E3N.pix.service.claim.mocks;

import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.service.claim.dto.ClaimDto;
import com.E3N.test.Owner.RandomCNPJMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomKeysMock;
import com.E3N.test.Owner.RandomParticipant;

import java.util.UUID;

public abstract class ClaimDtoMock {

    public static ClaimDto getDto(TypePerson typePerson) {
        if (TypePerson.NATURAL_PERSON.equals(typePerson)) {
            return new ClaimDto(
                    UUID.randomUUID().toString(),
                    RandomParticipant.getParticipant(),
                    RandomParticipant.getParticipant(),
                    RandomKeysMock.randomEmails(),
                    RandomCpfMock.getRandomCFP(),
                    TypeClaim.PORTABILITY,
                    TypeKey.EMAIL,
                    TypePerson.NATURAL_PERSON
            );
        }
        return new ClaimDto(
                UUID.randomUUID().toString(),
                RandomParticipant.getParticipant(),
                RandomParticipant.getParticipant(),
                RandomKeysMock.randomEmails(),
                RandomCNPJMock.getRandomCNPJ(),
                TypeClaim.PORTABILITY,
                TypeKey.EMAIL,
                TypePerson.LEGAL_PERSON
        );
    }

    public static ClaimDto getInvalidDto(TypePerson typePerson) {
        if (TypePerson.NATURAL_PERSON.equals(typePerson)) {
            return new ClaimDto(
                    UUID.randomUUID().toString(),
                    RandomParticipant.getParticipant(),
                    RandomParticipant.getParticipant(),
                    RandomKeysMock.randomInvalidEmails(),
                    RandomCpfMock.getRandomInvalidCPF(),
                    TypeClaim.OWNERSHIP,
                    TypeKey.EMAIL,
                    TypePerson.NATURAL_PERSON
            );
        }
        return new ClaimDto(
                UUID.randomUUID().toString(),
                RandomParticipant.getParticipant(),
                RandomParticipant.getParticipant(),
                RandomKeysMock.randomInvalidEmails(),
                RandomCNPJMock.getRandomInvalidCNPJ(null),
                TypeClaim.OWNERSHIP,
                TypeKey.EMAIL,
                TypePerson.LEGAL_PERSON
        );
    }
}
