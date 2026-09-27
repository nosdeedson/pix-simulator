package com.E3N.pix.application.modules.ownership.mock;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.service.ownership.dto.AccountDto;
import com.E3N.pix.service.ownership.dto.EntryKeyDto;
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

    public static AccountDto from(final Account acc, EntryKeyDto dto){
        return new AccountDto(
                acc.getBranch().getBranch(),
                acc.getNumber().getNumber(),
                acc.getOpeningDate().toString().replace("T", " ").replace("Z", ""),
                acc.getParticipant().getParticipant(),
                acc.getType(),
                EntryKeyDtoMock.fromDto(dto)
        );
    }

    public static AccountDto getInvalid(){
        return new AccountDto("123456", RandomAccountMock.randomAccountNumber(), RandomDateMock.getRandomStringDateWithoutTimeZone(),
                RandomParticipant.getParticipant(), AccountType.CACC, EntryKeyDtoMock.getEntryKeyDto(TypeKey.EMAIL));
    }
}
