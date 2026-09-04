package com.E3N.pix.service.owner.mocks.dto;

import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.owner.dto.EntryKeyDto;
import com.E3N.test.Owner.RandomKeysMock;

import java.util.UUID;

public abstract class EntryKeyDtoMock {

    private static EntryKeyDto getEntryKey(
            final String key,
            final TypeKey typeKey
    ) {
        return new EntryKeyDto(key, typeKey, Reason.USER_REQUESTED, UUID.randomUUID().toString());
    }

    public static EntryKeyDto getEntryKeyDto(final TypeKey typeKey) {
        switch (typeKey) {
            case CPF -> {
                return getEntryKeyNaturalPersonDto();
            }
            case EVP -> {
                return getEntryKeyEVPDto();
            }
            case CNPJ -> {
                return getEntryKeyLegalPersonDto();
            }
            case EMAIL -> {
                return getEntryKeyEmailDto();
            }
            case PHONE -> {
                return getEntryKeyPhoneDto();
            }
        }
        return getEntryKeyEmailDto();
    }

    private static EntryKeyDto getEntryKeyEmailDto() {
        return getEntryKey(
                RandomKeysMock.randomEmails(),
                TypeKey.EMAIL
        );
    }

    private static EntryKeyDto getEntryKeyPhoneDto() {
        return getEntryKey(
                RandomKeysMock.randomPhone(),
                TypeKey.PHONE
        );
    }

    private static EntryKeyDto getEntryKeyNaturalPersonDto() {
        return getEntryKey(
                RandomKeysMock.randomNaturalPersonDocument(),
                TypeKey.CPF
        );
    }

    private static EntryKeyDto getEntryKeyLegalPersonDto() {
        return getEntryKey(
                RandomKeysMock.randomLegalPersonDocument(),
                TypeKey.CNPJ
        );
    }

    private static EntryKeyDto getEntryKeyEVPDto() {
        return getEntryKey(
                UUID.randomUUID().toString(),
                TypeKey.EVP
        );
    }
}
