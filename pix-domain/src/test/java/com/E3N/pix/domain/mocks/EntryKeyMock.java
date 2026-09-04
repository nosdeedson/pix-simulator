package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.RandomKeysMock;

import java.util.UUID;

public abstract class EntryKeyMock {
    public static EntryKey getEntryKeyEmail() {
        return EntryKey.getInstance(
                RandomKeysMock.randomEmails(),
                TypeKey.EMAIL,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyInvalidEmail() {
        return EntryKey.getInstance(
                RandomKeysMock.randomInvalidEmails(),
                TypeKey.EMAIL,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyEVP() {
        return EntryKey.getInstance(
                RandomKeysMock.randomEVP(),
                TypeKey.EVP,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyPhone() {
        return EntryKey.getInstance(
                RandomKeysMock.randomPhone(),
                TypeKey.PHONE,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyCpf() {
        return EntryKey.getInstance(
                RandomKeysMock.randomNaturalPersonDocument(),
                TypeKey.CPF,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyCpf(final String docNaturalPerson) {
        return EntryKey.getInstance(
                docNaturalPerson,
                TypeKey.CPF,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyCnpj(final String docLegalPerson) {
        return EntryKey.getInstance(
                docLegalPerson,
                TypeKey.CNPJ,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyCnpj() {
        return EntryKey.getInstance(
                RandomKeysMock.randomLegalPersonDocument(),
                TypeKey.CNPJ,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyOfLegalPerson(final String docLegalPerson) {
        return EntryKey.getInstance(
                docLegalPerson,
                TypeKey.CNPJ,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getInvalidEntryKeyCnpj(final Integer whichOne) {
        return EntryKey.getInstance(
                RandomKeysMock.randomInvalidLegalPersonDocument(whichOne),
                TypeKey.CNPJ,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }
}
