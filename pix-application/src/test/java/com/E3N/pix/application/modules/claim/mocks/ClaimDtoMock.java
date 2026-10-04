package com.E3N.pix.application.modules.claim.mocks;

import com.E3N.pix.application.claim.dto.ClaimDto;
import com.E3N.pix.application.claim.dto.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.ClaimerDto;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.test.Owner.*;

public abstract class ClaimDtoMock {

    public static ClaimerAccountDto getAcc(String accountNumber) {
        return new ClaimerAccountDto(
                RandomParticipant.getParticipant(),
                RandomAccountMock.randomBranch(),
                accountNumber == null ? RandomAccountMock.randomAccountNumber() : accountNumber,
                AccountType.CACC,
                RandomDateMock.getRandomStringDateWithoutTimeZone()
        );
    }

    public static ClaimerDto getClaimer(TypePerson typePerson) {
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            var name = RandomValidName.randomValidCompanyName();
            return new ClaimerDto(
                    typePerson,
                    RandomCNPJMock.getRandomCNPJ(),
                    name,
                    name
            );
        }
        return new ClaimerDto(
                TypePerson.NATURAL_PERSON,
                RandomCpfMock.getRandomCFP(),
                RandomValidName.randomValidName(),
                null
        );
    }

    public static ClaimDto getClaimDto(
            TypePerson typePerson,
            TypeClaim typeClaim,
            TypeKey typeKey,
            String accountNumber,
            String key
    ) {
        return new ClaimDto(
                typeClaim,
                key == null ? getTypeKey(typeKey) : key,
                typeKey,
                getAcc(accountNumber),
                getClaimer(typePerson)
        );
    }

    private static String getTypeKey(TypeKey typeKey){
        String key = null;
        switch (typeKey){
            case CPF -> key =  RandomKeysMock.randomNaturalPersonDocument();
            case EVP -> key =  RandomKeysMock.randomEVP();
            case CNPJ -> key =  RandomKeysMock.randomLegalPersonDocument();
            case EMAIL -> key =  RandomKeysMock.randomEmails();
            case PHONE -> key =  RandomKeysMock.randomPhone();
        }
        return key;
    }
}
