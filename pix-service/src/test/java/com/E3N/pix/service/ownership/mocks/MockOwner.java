package com.E3N.pix.service.ownership.mocks;

import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.service.ownership.dto.OwnerDto;

public abstract class MockOwner {

    public static Owner get(OwnerDto dto) {
        var account = dto.account().toEntity();
        return Owner.getInstance(
                dto.name(),
                dto.tradeName(),
                dto.taxIdNumber(),
                dto.typePerson(),
                account
        );
    }
}
