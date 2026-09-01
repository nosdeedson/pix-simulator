package com.E3N.pix.domain.mocks.domain;

import com.E3N.pix.domain.modules.entry.entryKey.EntryKey;
import com.E3N.pix.domain.modules.entry.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.entrykey.RandomKeys;

import java.util.UUID;

public abstract class EntryKeyMock {

    public static EntryKey getEntryKeyEmail() {
        return EntryKey.getInstance(
                RandomKeys.randomEmails(),
                TypeKey.EMAIL,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyInvalidEmail() {
        return EntryKey.getInstance(
                RandomKeys.randomInvalidEmails(),
                TypeKey.EMAIL,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyEVP() {
        return EntryKey.getInstance(
                RandomKeys.randomEVP(),
                TypeKey.EVP,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyPhone() {
        return EntryKey.getInstance(
                RandomKeys.randomPhone(),
                TypeKey.PHONE,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }

    public static EntryKey getEntryKeyCpf() {
        return EntryKey.getInstance(
                RandomKeys.randomNaturalPersonDocument(),
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
                RandomKeys.randomLegalPersonDocument(),
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
                RandomKeys.randomInvalidLegalPersonDocument(whichOne),
                TypeKey.CNPJ,
                Reason.USER_REQUESTED,
                UUID.randomUUID().toString()
        );
    }
}
