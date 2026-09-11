package com.E3N.pix.infrastructure.modules.account;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.infrastructure.GenericEntity;
import com.E3N.pix.infrastructure.modules.entryKey.EntryKeyJPAEntity;
import com.E3N.pix.infrastructure.modules.owner.OwnerJPAEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Account")
@Table(name = "account")
public class AccountJPAEntity extends GenericEntity {

    @Column(nullable = false, length = 4)
    private String branch;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(name = "opening_date")
    private Instant openingDate;

    @Column(length = 8)
    private String participant;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntryKeyJPAEntity> keyJPAs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_owner"))
    private OwnerJPAEntity owner;

    public AccountJPAEntity() {
    }

    private AccountJPAEntity(
            String id,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            String branch,
            String number,
            Instant openingDate,
            String participant,
            AccountType type,
            OwnerJPAEntity ownerJPA
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.branch = branch;
        this.number = number;
        this.openingDate = openingDate;
        this.participant = participant;
        this.type = type;
        this.owner = ownerJPA;
    }

    public static AccountJPAEntity from(Account account, OwnerJPAEntity owner) {
        List<EntryKeyJPAEntity> keys = new ArrayList<>(account.getEntryKeys().size());
        var acc = new AccountJPAEntity(
                account.getId().toString(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getDeletedAt(),
                account.getBranch().getBranch(),
                account.getNumber().getNumber(),
                account.getOpeningDate(),
                account.getParticipant().getParticipant(),
                account.getType(),
                owner
        );
        keys = EntryKeyJPAEntity.fromList(account.getEntryKeys(), acc);
        acc.setKeyJPAs(keys);
        return acc;
    }

    public static List<AccountJPAEntity> from(List<Account> accounts, OwnerJPAEntity ownerJPA) {
        List<AccountJPAEntity> jpaEntities = new ArrayList<>(accounts.size());
        accounts.forEach(it -> jpaEntities.add(AccountJPAEntity.from(it, ownerJPA)));
        return jpaEntities;
    }

    public static Account from(AccountJPAEntity accountJPA) {
        List<EntryKey> keys = EntryKeyJPAEntity.fromEntities(accountJPA.getKeyJPAs());
        return Account.getInstance(
                UUID.fromString(accountJPA.getId()),
                accountJPA.getCreatedAt(),
                accountJPA.getUpdatedAt(),
                accountJPA.getDeletedAt(),
                accountJPA.getBranch(),
                accountJPA.getNumber(),
                accountJPA.getParticipant(),
                accountJPA.getType(),
                accountJPA.getOpeningDate(),
                keys
        );
    }

    public static List<Account> fromEntities(List<AccountJPAEntity> jpaEntities) {
        List<Account> accounts = new ArrayList<>(jpaEntities.size());
        jpaEntities.forEach(it -> accounts.add(AccountJPAEntity.from(it)));
        return accounts;
    }

    public String getBranch() {
        return branch;
    }

    public String getNumber() {
        return number;
    }

    public Instant getOpeningDate() {
        return openingDate;
    }

    public String getParticipant() {
        return participant;
    }

    public AccountType getType() {
        return type;
    }

    public List<EntryKeyJPAEntity> getKeyJPAs() {
        return keyJPAs;
    }

    public OwnerJPAEntity getOwner() {
        return owner;
    }

    public void setOwner(OwnerJPAEntity owner) {
        this.owner = owner;
    }

    public void setKeyJPAs(List<EntryKeyJPAEntity> keyJPAs) {
        this.keyJPAs = keyJPAs;
    }
}
