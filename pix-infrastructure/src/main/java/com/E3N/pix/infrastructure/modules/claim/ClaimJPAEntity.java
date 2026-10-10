package com.E3N.pix.infrastructure.modules.claim;

import com.E3N.pix.domain.modules.claim.Claim;
import com.E3N.pix.domain.modules.claim.StatusClaim;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.infrastructure.GenericEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity(name = "Claim")
@Table(name = "claim")
public class ClaimJPAEntity extends GenericEntity {

    @Column(nullable = false, name = "claimer_account_id")
    private String claimerAccountId;

    @Column(nullable = false, name = "claimer_participant", length = 8)
    private String claimerParticipant;

    @Column(nullable = false, name = "completion_period_end")
    private Instant completionPeriodEnd;

    @Column(nullable = false, name = "donor_participant", length = 8)
    private String donorParticipant;

    @Column(nullable = false, name = "key_claimed")
    private String keyClaimed;

    @Column(nullable = false, name = "resolution_period_end")
    private Instant resolutionPeriodEnd;

    @Column(nullable = false, name = "status")
    private StatusClaim status;

    @Column(nullable = false, name = "tax_id_number_claimer", length = 14)
    private String taxIdNumberClaimer;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private TypeClaim type;

    @Column(nullable = false, name = "type_key_claimed")
    @Enumerated(EnumType.STRING)
    private TypeKey typeKeyClaimed;

    @Column(nullable = false, name = "type_person")
    @Enumerated(EnumType.STRING)
    private TypePerson typePerson;

    public ClaimJPAEntity() {
    }

    private ClaimJPAEntity(
            String id,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            String claimerAccountId,
            String claimerParticipant,
            Instant completionPeriodEnd,
            String donorParticipant,
            String keyClaimed,
            Instant resolutionPeriodEnd,
            StatusClaim status,
            String taxIdNumberClaimer,
            TypeClaim type,
            TypeKey typeKeyClaimed,
            TypePerson typePerson
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.claimerAccountId = claimerAccountId;
        this.claimerParticipant = claimerParticipant;
        this.completionPeriodEnd = completionPeriodEnd;
        this.donorParticipant = donorParticipant;
        this.keyClaimed = keyClaimed;
        this.resolutionPeriodEnd = resolutionPeriodEnd;
        this.status = status;
        this.taxIdNumberClaimer = taxIdNumberClaimer;
        this.type = type;
        this.typeKeyClaimed = typeKeyClaimed;
        this.typePerson = typePerson;
    }

    public static ClaimJPAEntity from(final Claim claim) {
        return new ClaimJPAEntity(
                claim.getId().toString(),
                claim.getCreatedAt(),
                claim.getUpdatedAt(),
                claim.getDeletedAt(),
                claim.getClaimerAccountId(),
                claim.getClaimerParticipant(),
                claim.getCompletionPeriodEnd(),
                claim.getDonorParticipant(),
                claim.getKeyClaimed(),
                claim.getResolutionPeriodEnd(),
                claim.getStatus(),
                claim.getTaxIdNumberClaimer().getTaxIdNumber(),
                claim.getType(),
                claim.getTypeKeyClaimed(),
                claim.getTypePerson()
        );
    }

    public static Claim from(final ClaimJPAEntity claimJPA) {
        return Claim.getInstance(
                claimJPA.getId(),
                claimJPA.getCreatedAt(),
                claimJPA.getUpdatedAt(),
                claimJPA.getDeletedAt(),
                claimJPA.claimerAccountId,
                claimJPA.claimerParticipant,
                claimJPA.completionPeriodEnd,
                claimJPA.donorParticipant,
                claimJPA.keyClaimed,
                claimJPA.getUpdatedAt(),
                claimJPA.resolutionPeriodEnd,
                claimJPA.status,
                claimJPA.taxIdNumberClaimer,
                claimJPA.type,
                claimJPA.typeKeyClaimed,
                claimJPA.typePerson
        );
    }

    public String getClaimerAccountId() {
        return claimerAccountId;
    }

    public String getClaimerParticipant() {
        return claimerParticipant;
    }

    public Instant getCompletionPeriodEnd() {
        return completionPeriodEnd;
    }

    public String getDonorParticipant() {
        return donorParticipant;
    }

    public String getKeyClaimed() {
        return keyClaimed;
    }

    public Instant getResolutionPeriodEnd() {
        return resolutionPeriodEnd;
    }

    public StatusClaim getStatus() {
        return status;
    }

    public String getTaxIdNumberClaimer() {
        return taxIdNumberClaimer;
    }

    public TypeClaim getType() {
        return type;
    }

    public TypeKey getTypeKeyClaimed() {
        return typeKeyClaimed;
    }

    public TypePerson getTypePerson() {
        return typePerson;
    }
}
