package com.E3N.pix.domain.account;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.valueObject.participant.Participant;

import java.time.Instant;

public class Account extends Entity {
    private final Participant participant; // ISPB
    private final String branch; // ag
    private final String number;
    private final AccountType type;
    private final Instant openingDate;

    private Account(
            final String branch,
            final String number,
            final Participant participant,
            final AccountType type,
            final Instant openingDate
    ) {
        super();
        this.participant = participant;
        this.branch = branch;
        this.number = number;
        this.type = type;
        this.openingDate = openingDate;
    }

    public static Account from(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final Instant openingDate
    ) {
        var participantInstance = Participant.getInstance(participant);
        return new Account(branch, number, participantInstance, type, openingDate);
    }

    public Participant getParticipant() {
        return participant;
    }

    public String getBranch() {
        return branch;
    }

    public String getNumber() {
        return number;
    }

    public AccountType getType() {
        return type;
    }

    public Instant getOpeningDate() {
        return openingDate;
    }

    @Override
    protected void validate() {

    }
}
