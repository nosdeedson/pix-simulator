package com.E3N.pix.domain.modules.ownership.dto;

import com.E3N.pix.domain.modules.ownership.account.AccountType;

public record UpdateAccountDto(
        String branch,
        String number,
        String participant,
        AccountType type,
        String openingDate
) {
}
