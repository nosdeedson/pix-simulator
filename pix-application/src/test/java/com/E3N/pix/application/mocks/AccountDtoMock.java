package com.E3N.pix.application.mocks;

import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.owner.dto.AccountDto;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomParticipant;

public abstract class AccountDtoMock {

    public static AccountDto mockAccountDto(final TypeKey typeKey) {
        return new AccountDto(
                RandomAccountMock.randomBranch(),
                RandomAccountMock.randomAccountNumber(),
                RandomDateMock.getRandomStringDateWithoutTimeZone(),
                RandomParticipant.getParticipant(),
                RandomAccountTypeMock.getAccountType(),
                EntryKeyDtoMock.getEntryKeyDto(typeKey)
        );
    }

    public static AccountDto mockAccountDto(final AccountDto dtoMock) {
        return new AccountDto(
                dtoMock.branch(),
                dtoMock.accountNumber(),
                dtoMock.openingDate(),
                "92874270",
                dtoMock.accountType(),
                dtoMock.entryKeyDto()
        );
    }
}
