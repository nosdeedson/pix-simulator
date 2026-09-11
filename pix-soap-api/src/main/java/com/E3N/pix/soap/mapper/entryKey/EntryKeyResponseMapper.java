package com.E3N.pix.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.soap.contract.*;
import com.E3N.pix.soap.endpoints.EntryKey;
import com.E3N.shared.utils.DateHelper;

public abstract class EntryKeyResponseMapper {

    private static OwnerType createOwnerType(Owner owner){
        OwnerType owerType = new OwnerType();
        owerType.setName(owner.getName().getName());
        if (owner.getTradeName() != null) owerType.setTradeName(owner.getTradeName().getName());
        owerType.setTaxIdNumber(owner.getTaxIdNumber().getTaxIdNumber());
        owerType.setType(OwnerTypeEnum.fromValue(owner.getType().name()));
        return owerType;
    }

    private static AccountType createAccountType(Account acc){
        AccountType accountType = new AccountType();
        accountType.setAccountNumber(acc.getNumber().getNumber());
        accountType.setAccountType(AccountTypeEnum.fromValue(acc.getType().name()));
        accountType.setBranch(acc.getBranch().getBranch());
        accountType.setOpeningDate(DateHelper.fromInstant(acc.getOpeningDate()));
        accountType.setParticipant(acc.getParticipant().getParticipant());
        return accountType;
    }

    public static CreateEntryKeyResponse from(Owner owner) {
        var response = new CreateEntryKeyResponse();
        var entryKeyType = new EntryResponseType();
        var entryKey = owner.getAccounts().getLast().getEntryKeys().getLast();
        entryKeyType.setCreationDate(DateHelper.fromInstant(entryKey.getCreationDate()));
        entryKeyType.setKeyOwnershipDate(DateHelper.fromInstant(entryKey.getKeyOwnershipDate()));
        entryKeyType.setKey(entryKey.getKey().getKey());
        entryKeyType.setKeyType(KeyType.valueOf(entryKey.getKey().getType().name()));

        // OwnerType
        entryKeyType.setOwner(createOwnerType(owner));

        // accountType
        entryKeyType.setAccount(createAccountType(owner.getAccounts().getLast()));

        response.setResponseTime(DateHelper.fromInstant(owner.getAccounts().getLast().getEntryKeys().getLast().getResponseTime()));
        response.setCorrelationId(owner.getAccounts().getLast().getEntryKeys().getLast().getCorrelationId());

        response.setEntry(entryKeyType);
        return response;
    }

}
