package com.E3N.pix.service.ownership.dto;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;

public record AccountDto(
        String branch,
        String accountNumber,
        String openingDate,
        String participant,
        AccountType accountType,
        EntryKeyDto entryKeyDto
) {
    public Account toEntity() {
        return Account.getInstance(
                this.branch(),
                this.accountNumber(),
                this.participant(),
                this.accountType(),
                this.openingDate(),
                this.entryKeyDto.toEntity()
        );
    }
}
