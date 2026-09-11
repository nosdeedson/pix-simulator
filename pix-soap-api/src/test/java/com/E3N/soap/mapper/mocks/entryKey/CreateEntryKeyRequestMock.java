package com.E3N.soap.mapper.mocks.entryKey;

import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;
import com.E3N.test.Owner.RandomCpfMock;

import java.time.Instant;
import java.util.UUID;

public abstract class CreateEntryKeyRequestMock {

    public static CreateEntryKeyRequest createRequest(boolean invalidTaxIdNumber) {
        var request = new CreateEntryKeyRequest();
        request.setSignature(UUID.randomUUID().toString());
        request.setEntry(getEntry(invalidTaxIdNumber));
        request.setReason(ReasonType.USER_REQUESTED);
        request.setRequestId(UUID.randomUUID().toString());

        return request;
    }

    private static EntryType getEntry(boolean invalidTaxIdNumber) {
        var entry = new EntryType();
        entry.setKey("gabriel.silva@gmail.com");
        entry.setKeyType(KeyType.EMAIL);
        entry.setAccount(getAccount());
        entry.setOwner(getOwner(invalidTaxIdNumber));

        return entry;
    }

    private static AccountType getAccount() {
        var account = new AccountType();
        account.setParticipant("8HTVVWKM");
        account.setBranch("1234");
        account.setAccountNumber("123456789");
        account.setAccountType(AccountTypeEnum.CACC);
        account.setOpeningDate(DateHelper.fromInstant(Instant.now()));

        return account;
    }

    private static OwnerType getOwner(boolean invalidTaxIdNumber) {
        var taxIdNumber = RandomCpfMock.getRandomCFP();
        if (invalidTaxIdNumber) taxIdNumber = RandomCpfMock.getRandomInvalidCPF();
        var owner = new OwnerType();
        owner.setType(OwnerTypeEnum.NATURAL_PERSON);
        owner.setTaxIdNumber(taxIdNumber);
        owner.setName("Gabriel Silva");

        return owner;
    }
}
