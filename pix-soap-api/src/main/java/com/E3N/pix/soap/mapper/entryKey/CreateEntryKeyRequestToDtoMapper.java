package com.E3N.pix.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.ownership.dto.AccountDto;
import com.E3N.pix.service.ownership.dto.EntryKeyDto;
import com.E3N.pix.service.ownership.dto.OwnerDto;
import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
import com.E3N.shared.utils.DateHelper;

public class CreateEntryKeyRequestToDtoMapper {

    public static OwnerDto from(CreateEntryKeyRequest request) {
        if (request.getEntry().getOwner().getType().name().equals(TypePerson.LEGAL_PERSON.name())) {
            return new OwnerDto(
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getTaxIdNumber(),
                    TypePerson.LEGAL_PERSON,
                    null,
                    CreateEntryKeyRequestToDtoMapper.createAccountDto(request)
            );
        } else {
            return new OwnerDto(
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getTaxIdNumber(),
                    TypePerson.NATURAL_PERSON,
                    null,
                    CreateEntryKeyRequestToDtoMapper.createAccountDto(request)
            );
        }
    }

    private static AccountDto createAccountDto(CreateEntryKeyRequest request) {
        var accountType = request.getEntry().getAccount();
        return new AccountDto(
                accountType.getBranch(),
                accountType.getAccountNumber(),
                DateHelper.fromGregorianCalendar(request.getEntry().getAccount().getOpeningDate()),
                accountType.getParticipant(),
                AccountType.valueOf(accountType.getAccountType().name()),
                createEntryDto(request)
        );
    }

    private static EntryKeyDto createEntryDto(CreateEntryKeyRequest request) {
        return new EntryKeyDto(
                request.getEntry().getKey(),
                TypeKey.valueOf(request.getEntry().getKeyType().name()),
                Reason.valueOf(request.getReason().name()),
                request.getRequestId()
        );
    }

}
