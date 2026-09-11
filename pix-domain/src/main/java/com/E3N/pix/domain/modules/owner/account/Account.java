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
import java.util.UUID;

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
        this.openingDate = DateHelper.getDateFrom(openingDate, "yyyy-MM-dd HH:mm:ss");
        this.entryKeys.add(entryKey);
        validate();
    }

    private Account(
            final UUID id,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final Instant openingDate,
            final List<EntryKey> keys
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.entryKeys = keys;
        this.participant = Participant.getInstance(participant);
        this.branch = Branch.getInstance(branch);
        this.number = AccountNumber.getInstance(number);
        this.type = type;
        this.openingDate = openingDate;
        this.entryKeys = keys;
    }

    /**
     * Will validate the instance
     *
     * @param branch      @description
     * @param number      @description
     * @param participant @description
     * @param type        @description
     * @param openingDate @description
     * @param entryKey    @description
     * @return @description will return a Domain Account
     */
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

    /**
     * Won't validate the instance, should be used to convert from BD to Domain
     *
     * @param id          @description
     * @param createdAt   @description
     * @param updatedAt   @description
     * @param deletedAt   @description
     * @param branch      @description
     * @param number      @description
     * @param participant @description
     * @param type        @description
     * @param openingDate @description
     * @param keys        @description
     * @return will return an instance using data from BD
     */
    public static Account getInstance(
            final UUID id,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final String branch,
            final String number,
            final String participant,
            final AccountType type,
            final Instant openingDate,
            final List<EntryKey> keys
    ) {
        return new Account(
                id, createdAt, updatedAt, deletedAt, branch, number, participant, type, openingDate, keys
        );
    }

    public void addKey(final EntryKey newKey) {
        this.entryKeys.add(newKey);
        this.validate();
    }

    /**
     * as one owner was found with a key, validating the account and participant
     * if both are equals owner is trying to create the same key
     *
     * @param participant   @description participant of the request
     * @param accountNumber @description accountNumber of the request
     * @return @description if participant of the account and the number are equals return true
     */
    public boolean sameParticipant(final String participant, final String accountNumber) {
        return this.participant.getParticipant().equals(participant)
                && this.number.getNumber().equals(accountNumber);
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

    @Override
    public String toString() {
        return "Account{" +
                "branch=" + branch +
                ", number=" + number +
                ", openingDate=" + openingDate +
                ", participant=" + participant +
                ", type=" + type +
                ", entryKeys=" + entryKeys +
                ", notification=" + notification +
                '}';
    }
}
