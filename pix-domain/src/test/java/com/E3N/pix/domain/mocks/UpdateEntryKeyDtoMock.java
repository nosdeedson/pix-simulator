package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.dto.UpdateAccountDto;
import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.test.Owner.*;

public abstract class UpdateEntryKeyDtoMock {

    public static UpdateEntryKeyDto getUpdateEntryKeyValid(String participant, EntryKey key, String taxIdNumber, Reason reason) {
        var dto = new UpdateAccountDto(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                participant, AccountType.CACC, "2026-09-14 12:54:25");

        return new UpdateEntryKeyDto(
                key.getKey().getKey(),
                RandomValidName.randomValidCompanyName(),
                RandomValidName.randomValidCompanyName(),
                TypePerson.LEGAL_PERSON,
                taxIdNumber,
                dto,
                reason
        );
    }

    public static UpdateEntryKeyDto getInvalidDto(Reason reason) {
        var dto = new UpdateAccountDto(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC, "2026-09-14 13:59:25");
        return new UpdateEntryKeyDto(
                RandomKeysMock.randomEmails(),
                RandomValidName.randomValidCompanyName(),
                RandomValidName.randomValidCompanyName(),
                TypePerson.LEGAL_PERSON,
                RandomCNPJMock.getRandomCNPJ(),
                dto,
                reason
        );
    }
}
