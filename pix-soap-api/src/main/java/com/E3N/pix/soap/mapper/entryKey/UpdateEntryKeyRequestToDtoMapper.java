package com.E3N.pix.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.dto.UpdateAccountDto;
import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.soap.contract.UpdateEntryKeyRequest;
import com.E3N.shared.utils.DateHelper;

public abstract class UpdateEntryKeyRequestToDtoMapper {

    private static UpdateAccountDto getAccount(UpdateEntryKeyRequest request) {
        var account = request.getAccount();
        return new UpdateAccountDto(account.getBranch(), account.getAccountNumber(), account.getParticipant(),
                AccountType.valueOf(account.getAccountType().name()), DateHelper.fromGregorianCalendar(account.getOpeningDate()));
    }

    public static UpdateEntryKeyDto getDto(UpdateEntryKeyRequest request) {
        var owner = request.getOwner();
        return new UpdateEntryKeyDto(request.getKey(), owner.getName(), owner.getTradeName(),
                TypePerson.valueOf(owner.getType().name()), owner.getTaxIdNumber(), getAccount(request),
                Reason.valueOf(request.getReason().name()));
    }
}
