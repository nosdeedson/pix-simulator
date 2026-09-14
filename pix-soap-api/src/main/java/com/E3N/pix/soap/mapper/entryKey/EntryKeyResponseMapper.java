package com.E3N.pix.soap.mapper.entryKey;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;
import java.util.UUID;

public abstract class EntryKeyResponseMapper {

    private static OwnerType createOwnerType(Owner owner) {
        OwnerType owerType = new OwnerType();
        owerType.setName(owner.getName().getName());
        if (owner.getTradeName() != null) owerType.setTradeName(owner.getTradeName().getName());
        owerType.setTaxIdNumber(owner.getTaxIdNumber().getTaxIdNumber());
        owerType.setType(OwnerTypeEnum.fromValue(owner.getType().name()));
        return owerType;
    }

    private static AccountType createAccountType(Account acc) {
        AccountType accountType = new AccountType();
        accountType.setAccountNumber(acc.getNumber().getNumber());
        accountType.setAccountType(AccountTypeEnum.fromValue(acc.getType().name()));
        accountType.setBranch(acc.getBranch().getBranch());
        accountType.setOpeningDate(DateHelper.fromInstant(acc.getOpeningDate()));
        accountType.setParticipant(acc.getParticipant().getParticipant());
        return accountType;
    }

    private static EntryResponseType createEntryResponseType(Owner owner) {
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
        return entryKeyType;
    }

    /**
     * convert an Owner to EntryKeyResponse
     *
     * @param owner @description domain Owner
     * @return CreateEntryKeyResponse
     */
    public static CreateEntryKeyResponse from(Owner owner) {
        var response = new CreateEntryKeyResponse();

        var entryKeyType = createEntryResponseType(owner);

        response.setResponseTime(DateHelper.fromInstant(owner.getAccounts().getLast().getEntryKeys().getLast().getResponseTime()));
        response.setCorrelationId(owner.getAccounts().getLast().getEntryKeys().getLast().getCorrelationId());

        response.setEntry(entryKeyType);
        return response;
    }

    /**
     * Convert an Owner to GetEntryKeyResponse
     *
     * @param owner @description Owner domain
     * @return GetEntryKeyResponse
     */
    public static GetEntryKeyResponse getEntryKeyResponse(Owner owner) {
        var response = new GetEntryKeyResponse();
        response.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        response.setResponseTime(DateHelper.fromInstant(Instant.now()));
        response.setSignature("not-for-now");

        var entryType = createEntryResponseType(owner);

        // TODO add the statistics

        response.setEntry(entryType);
        return response;
    }
}
