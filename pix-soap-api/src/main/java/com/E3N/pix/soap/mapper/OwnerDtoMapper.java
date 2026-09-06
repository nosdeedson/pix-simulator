package com.E3N.pix.soap.mapper;

import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.owner.dto.AccountDto;
import com.E3N.pix.service.owner.dto.EntryKeyDto;
import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.pix.soap.contract.CreateEntryKeyRequest;
import com.E3N.pix.domain.modules.owner.account.AccountType;

public class OwnerDtoMapper {

    public static OwnerDto from(CreateEntryKeyRequest request) {
        if (request.getEntry().getOwner().getType().name().equals(TypePerson.LEGAL_PERSON.name())) {
            return new OwnerDto(
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getTaxIdNumber(),
                    TypePerson.LEGAL_PERSON,
                    null,
                    OwnerDtoMapper.createAccountDto(request)
            );
        } else {
            return new OwnerDto(
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getName(),
                    request.getEntry().getOwner().getTaxIdNumber(),
                    TypePerson.NATURAL_PERSON,
                    null,
                    OwnerDtoMapper.createAccountDto(request)
            );
        }
    }

    private static AccountDto createAccountDto(CreateEntryKeyRequest request){
        var accountType = request.getEntry().getAccount();
        return new AccountDto(
                accountType.getBranch(),
                accountType.getAccountNumber(),
                accountType.getOpeningDate().toString(),
                accountType.getParticipant(),
                AccountType.valueOf(accountType.getAccountType().name()),
                createEntryDto(request)
        );
    }

    private static EntryKeyDto createEntryDto(CreateEntryKeyRequest request){
        return new EntryKeyDto(
                request.getEntry().getKey(),
                TypeKey.valueOf(request.getEntry().getKeyType().name()),
                Reason.valueOf(request.getReason().name()),
          request.getRequestId()
        );
    }

}
