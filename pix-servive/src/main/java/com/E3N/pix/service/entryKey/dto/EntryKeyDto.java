package com.E3N.pix.service.entryKey.dto;

import com.E3N.pix.domain.modules.entry.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;

public record EntryKeyDto(
        AccountDto accountDto,
        String creationDate,
        String key,
        TypeKey typeKey,
        OwnerDto ownerDto,
        Reason reason,
        String requestId
) {
}
