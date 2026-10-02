package com.E3N.pix.application.claim.dto;

import com.E3N.pix.domain.modules.ownership.account.AccountType;

public record ClaimerAccountDto(
        String participant,
        String branch,
        String accountNumber,
        AccountType accountType,
        String openingDate
) {
}
