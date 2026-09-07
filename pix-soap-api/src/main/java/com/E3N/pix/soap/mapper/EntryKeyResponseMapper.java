package com.E3N.pix.soap.mapper;

import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

public abstract class EntryKeyResponseMapper {

    public static CreateEntryKeyResponse from(Owner owner) {
        var response = new CreateEntryKeyResponse();
        var entryKey = new EntryResponseType();
        entryKey.setCreationDate(DateHelper.fromInstant(owner.getCreatedAt()));
        entryKey.setKeyOwnershipDate(DateHelper.fromInstant(owner.getAccounts().getLast().getEntryKeys().getLast().getKeyOwnershipDate()));
        entryKey.setKey(owner.getAccounts().getLast().getEntryKeys().getLast().getKey().getKey());
        entryKey.setKeyType(KeyType.valueOf(owner.getAccounts().getLast().getEntryKeys().getLast().getKey().getType().name()));
        // OwnerType
        OwnerType owerType = new OwnerType();

        owerType.setName(owerType.getName());
        if (owner.getTradeName() != null) owerType.setTradeName(owerType.getTradeName());
        owerType.setTaxIdNumber(owerType.getTaxIdNumber());
        owerType.setType(OwnerTypeEnum.fromValue(owner.getType().name()));

        entryKey.setOwner(owerType);

        // accountType
        AccountType accountType = new AccountType();
        var acc = owner.getAccounts().getLast();
        accountType.setAccountNumber(acc.getNumber().getNumber());
        accountType.setAccountType(AccountTypeEnum.fromValue(acc.getType().name()));
        accountType.setBranch(acc.getBranch().getBranch());
        accountType.setOpeningDate(DateHelper.fromInstant(acc.getOpeningDate()));
        accountType.setParticipant(acc.getParticipant().getParticipant());
        entryKey.setAccount(accountType);

        response.setEntry(entryKey);
        return response;
    }

}
