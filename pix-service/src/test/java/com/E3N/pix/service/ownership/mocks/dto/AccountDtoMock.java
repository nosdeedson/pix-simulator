package com.E3N.pix.service.ownership.mocks.dto;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.service.ownership.dto.AccountDto;
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

    public static AccountDto fromAccount(Account account){
        return new AccountDto(
                account.getBranch().getBranch(),
                account.getNumber().getNumber(),
                RandomDateMock.getRandomStringDateWithoutTimeZone(),
                account.getParticipant().getParticipant(),
                account.getType(),
                EntryKeyDtoMock.fromEntryKey(account.getEntryKeys().getFirst())
        );
    }
}
