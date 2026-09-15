package com.E3N.pix.domain.modules.ownership.dto;

import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;

public record UpdateEntryKeyDto(String key,
                                String name,
                                String tradeName,
                                TypePerson typePerson,
                                String taxIdNumber,
                                UpdateAccountDto accountDto,
                                Reason reason
) {
}
