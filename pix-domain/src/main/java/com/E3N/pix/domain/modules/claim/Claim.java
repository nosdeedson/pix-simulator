package com.E3N.pix.domain.modules.claim;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Claim extends Entity {

    private UUID id;
    private String claimerAccountId;
    private final String claimerParticipant;
    private Instant completionPeriodEnd;
    private String donorParticipant;
    private String keyClaimed;
    private Instant lastModified;
    private Instant resolutionPeriodEnd;
    private StatusClaim status;
    private TaxIdNumber taxIdNumberClaimer;
    private TypeClaim type;
    private TypeKey typeKeyClaimed;
    private TypePerson typePerson;

    private Notification notification;

    private Claim(
            String claimerAccountId,
            String claimerParticipant,
            String donorParticipant,
            String keyClaimed,
            String taxIdNumberClaimer,
            TypeClaim type,
            TypeKey typeKeyClaimed,
            TypePerson typePerson
    ) {
        super();
        this.claimerAccountId = claimerAccountId;
        this.claimerParticipant = claimerParticipant;
        if (TypeClaim.OWNERSHIP.equals(type)) {
            this.completionPeriodEnd = Instant.now().plus(7, ChronoUnit.DAYS);
        }
        this.donorParticipant = donorParticipant;
        this.keyClaimed = keyClaimed;
        this.lastModified = Instant.now();
        this.resolutionPeriodEnd = Instant.now().plus(7, ChronoUnit.DAYS);
        this.completionPeriodEnd = Instant.now();
        this.status = StatusClaim.OPEN;
        this.taxIdNumberClaimer = TaxIdNumber.getInstance(typePerson, taxIdNumberClaimer);
        this.type = type;
        this.typeKeyClaimed = typeKeyClaimed;
        this.typePerson = typePerson;
        validate();
    }

    public static Claim getInstance(
            String claimerAccountId,
            String claimerParticipant,
            String donorParticipant,
            String keyClaimed,
            String taxIdNumberClaimer,
            TypeClaim type,
            TypeKey typeKeyClaimed,
            TypePerson typePerson
    ) {
        return new Claim(
                claimerAccountId,
                claimerParticipant,
                donorParticipant,
                keyClaimed,
                taxIdNumberClaimer,
                type,
                typeKeyClaimed,
                typePerson
        );
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new ClaimValidator(this).validate();
        if (this.notification.hasError()) {
            this.id = null;
            this.claimerAccountId = null;
            this.completionPeriodEnd = null;
            this.donorParticipant = null;
            this.keyClaimed = null;
            this.lastModified = null;
            this.resolutionPeriodEnd = null;
            this.status = null;
            this.taxIdNumberClaimer = null;
            this.type = null;
            this.typeKeyClaimed = null;
            this.typePerson = null;
        }
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

    public Instant getLastModified() {
        return lastModified;
    }

    public Instant getResolutionPeriodEnd() {
        return resolutionPeriodEnd;
    }

    public StatusClaim getStatus() {
        return status;
    }

    public TaxIdNumber getTaxIdNumberClaimer() {
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

    public Notification getNotification() {
        return notification;
    }
}
