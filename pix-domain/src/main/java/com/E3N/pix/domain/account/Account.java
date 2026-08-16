package com.E3N.pix.domain.account;
import com.E3N.pix.domain.Entity;

import java.time.Instant;
import java.util.UUID;

public class Account extends Entity {
    private final String participant; // ISPB
    private final String branch; // ag
    private final String number;
    private final AccountType type;
    private final Instant openingDate;

    public Account(final UUID id,
                   final Instant createdAt,
                   final Instant updatedAt,
                   final Instant deletedAt,
                   final String participant,
                   final String branch,
                   final String number,
                   final AccountType type,
                   final Instant openingDate) {
        super(id, createdAt, updatedAt, deletedAt);
        this.participant = participant;
        this.branch = branch;
        this.number = number;
        this.type = type;
        this.openingDate = openingDate;
    }
}
