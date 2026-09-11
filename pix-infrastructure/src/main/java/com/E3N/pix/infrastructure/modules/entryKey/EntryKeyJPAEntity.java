package com.E3N.pix.infrastructure.modules.entryKey;

import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.infrastructure.GenericEntity;
import com.E3N.pix.infrastructure.modules.account.AccountJPAEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "entry_key")
@Table(name = "entry_key")
public class EntryKeyJPAEntity extends GenericEntity {

    @Column(nullable = false, name = "entry_key")
    private String key;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeKey type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(nullable = false, name = "request_id")
    private String requestId;

    @Column(nullable = false, name = "correlation_id")
    private String correlationId;

    @Column(nullable = false, name = "response_time")
    private Instant responseTime;

    @Column(nullable = false, name = "key_ownership_date")
    private Instant keyOwnershipDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_entryKey_account"))
    private AccountJPAEntity account;

    public EntryKeyJPAEntity() {
    }

    public EntryKeyJPAEntity(
            String id,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            String key,
            TypeKey typeKey,
            Reason reason,
            String requestId,
            String correlationId,
            Instant responseTime,
            Instant keyOwnershipDate,
            AccountJPAEntity accountJPA
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.key = key;
        this.type = typeKey;
        this.reason = reason;
        this.requestId = requestId;
        this.correlationId = correlationId;
        this.responseTime = responseTime;
        this.keyOwnershipDate = keyOwnershipDate;
        account = accountJPA;
    }

    public static EntryKeyJPAEntity from(EntryKey key, AccountJPAEntity accountJPA) {
        return new EntryKeyJPAEntity(
                key.getId().toString(),
                key.getCreatedAt(),
                key.getUpdatedAt(),
                null,
                key.getKey().getKey(),
                key.getKey().getType(),
                key.getReason(),
                key.getRequestId().toString(),
                key.getCorrelationId(),
                key.getResponseTime(),
                key.getKeyOwnershipDate(),
                accountJPA
        );
    }

    public static List<EntryKeyJPAEntity> fromList(List<EntryKey> keys, AccountJPAEntity accountJPA) {
        List<EntryKeyJPAEntity> jpaEntities = new ArrayList<>(keys.size());
        keys.forEach(entryKey -> jpaEntities.add(EntryKeyJPAEntity.from(entryKey, accountJPA)));
        return jpaEntities;
    }

    public static EntryKey from(EntryKeyJPAEntity entity) {
        return EntryKey.getInstance(
                UUID.fromString(entity.getId()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.key,
                entity.type,
                entity.getCreatedAt(),
                entity.reason,
                entity.requestId,
                entity.correlationId,
                entity.responseTime,
                entity.keyOwnershipDate
        );
    }

    public static List<EntryKey> fromEntities(List<EntryKeyJPAEntity> entities) {
        List<EntryKey> entryKeys = new ArrayList<>(entities.size());
        entities.forEach(entryKey -> entryKeys.add(EntryKeyJPAEntity.from(entryKey)));
        return entryKeys;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TypeKey getType() {
        return type;
    }

    public void setType(TypeKey type) {
        this.type = type;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Instant getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Instant responseTime) {
        this.responseTime = responseTime;
    }

    public Instant getKeyOwnershipDate() {
        return keyOwnershipDate;
    }

    public void setKeyOwnershipDate(Instant keyOwnershipDate) {
        this.keyOwnershipDate = keyOwnershipDate;
    }
}
