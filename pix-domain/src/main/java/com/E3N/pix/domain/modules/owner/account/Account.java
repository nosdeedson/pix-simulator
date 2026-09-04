package com.E3N.pix.domain.modules.owner.account;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.branch.Branch;
import com.E3N.pix.domain.valueObject.number.AccountNumber;
import com.E3N.pix.domain.valueObject.participant.Participant;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Account extends Entity {
    private Branch branch; // ag
    private final AccountNumber number;
    private Instant openingDate;
    private Participant participant; // ISPB
    private AccountType type;
    private List<EntryKey> entryKeys;

    private Notification notification;

    private Account(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String openingDate,
            final EntryKey entryKey
    ) {
        super();
        if (this.entryKeys == null)
            this.entryKeys = new ArrayList<>();
        this.participant = Participant.getInstance(participant);
        this.branch = Branch.getInstance(branch);
        this.number = AccountNumber.getInstance(number);
        this.type = type;
        this.openingDate = DateHelper.getDateFrom(openingDate, "dd/MM/yyyy");
        this.entryKeys.add(entryKey);
        validate();
    }

    public static Account getInstance(
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final String openingDate,
            final EntryKey entryKey
    ) {
        return new Account(branch, number, participant, type, openingDate, entryKey);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new AccountValidator(this).validate();
        if (this.notification.hasError()) {
            this.branch = null;
            this.type = null;
            this.participant = null;
            this.openingDate = null;
        }
    }

    public Participant getParticipant() {
        return participant;
    }

    public Branch getBranch() {
        return branch;
    }

    public AccountNumber getNumber() {
        return number;
    }

    public AccountType getType() {
        return type;
    }

    public Instant getOpeningDate() {
        return openingDate;
    }

    protected void setOpeningDate(Instant openingDate) {
        this.openingDate = openingDate;
    }

    public Notification getNotification() {
        return notification;
    }

    public List<EntryKey> getEntryKeys() {
        return entryKeys;
    }
}
