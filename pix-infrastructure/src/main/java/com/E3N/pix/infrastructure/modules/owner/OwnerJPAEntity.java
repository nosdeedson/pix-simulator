package com.E3N.pix.infrastructure.modules.owner;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.infrastructure.GenericEntity;
import com.E3N.pix.infrastructure.modules.account.AccountJPAEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity(name = "Owner")
@Table(name = "owner")
public class OwnerJPAEntity extends GenericEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String tradeName;

    @Column(nullable = true)
    private Instant openClaimCreationDate;

    @Column(nullable = false, length = 14, unique = true)
    private String taxIdNumber;

    @Column(name = "type_person", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePerson typePerson;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountJPAEntity> accounts = new ArrayList<>();

    public OwnerJPAEntity() {
    }

    private OwnerJPAEntity(
            String id,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            String name,
            String tradeName,
            TypePerson typePerson,
            Instant openClaimCreationDate,
            String taxIdNumber,
            List<AccountJPAEntity> accounts
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = name;
        this.tradeName = tradeName;
        this.openClaimCreationDate = openClaimCreationDate;
        this.taxIdNumber = taxIdNumber;
        this.typePerson = typePerson;
        this.accounts.addAll(accounts);
    }

    public static OwnerJPAEntity from(Owner owner) {
        List<AccountJPAEntity> jpaEntities = new ArrayList<>(owner.getAccounts().size());
        var ownerJPA = new OwnerJPAEntity(
                owner.getId().toString(),
                owner.getCreatedAt(),
                owner.getUpdatedAt(),
                null,
                owner.getName().getName(),
                (owner.getTradeName() != null ? owner.getTradeName().getName() : null),
                owner.getType(),
                owner.getOpenClaimCreationDate(),
                owner.getTaxIdNumber().getTaxIdNumber(),
                jpaEntities
        );
        jpaEntities = AccountJPAEntity.from(owner.getAccounts(), ownerJPA);
        ownerJPA.setAccounts(jpaEntities);
        return ownerJPA;
    }

    public static Owner from(OwnerJPAEntity ownerJPA) {
        List<Account> accountList = AccountJPAEntity.fromEntities(ownerJPA.getAccounts());
        return Owner.getInstance(
                UUID.fromString(ownerJPA.getId()),
                ownerJPA.getCreatedAt(),
                ownerJPA.getUpdatedAt(),
                ownerJPA.getDeletedAt(),
                ownerJPA.name,
                ownerJPA.tradeName,
                ownerJPA.taxIdNumber,
                ownerJPA.getTypePerson(),
                accountList
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Instant getOpenClaimCreationDate() {
        return openClaimCreationDate;
    }

    public void setOpenClaimCreationDate(Instant openClaimCreationDate) {
        this.openClaimCreationDate = openClaimCreationDate;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public TypePerson getTypePerson() {
        return typePerson;
    }

    public void setTypePerson(TypePerson typePerson) {
        this.typePerson = typePerson;
    }

    public List<AccountJPAEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountJPAEntity> accounts) {
        this.accounts = accounts;
    }
}
