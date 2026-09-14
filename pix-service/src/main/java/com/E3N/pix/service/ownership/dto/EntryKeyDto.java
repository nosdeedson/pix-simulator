package com.E3N.pix.service.ownership.dto;

import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;

public record EntryKeyDto(
        String key,
        TypeKey typeKey,
        Reason reason,
        String requestId
) {
    public EntryKey toEntity() {
        return EntryKey.getInstance(key, typeKey, reason, requestId);
    }
}
