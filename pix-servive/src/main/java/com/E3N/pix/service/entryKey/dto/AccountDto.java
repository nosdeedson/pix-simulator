package com.E3N.pix.service.entryKey.dto;

import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.account.AccountType;

public record AccountDto(
        String branch,
        String accountNumber,
        String openingDate,
        String participant,
        AccountType accountType
) {
    public Account toEntity() {
        return Account.getInstance(
                this.branch(),
                this.accountNumber(),
                this.participant(),
                this.accountType(),
                this.openingDate()
        );
    }
}
