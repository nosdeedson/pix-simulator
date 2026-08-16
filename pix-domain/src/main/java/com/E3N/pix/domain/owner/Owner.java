package com.E3N.pix.domain.owner;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.taxIdNumber.TaxIdNumber;

import java.time.Instant;
import java.util.UUID;

public class Owner extends Entity {
    private final TypePerson type;
    private final TaxIdNumber taxIdNumber; // cpf/cnpj
    private final String name;
    private final Instant keyOwnerShipDate;
    private Instant openClaimCreateionDate;

    public Owner(final UUID id,
                 final Instant createdAt,
                 final Instant updatedAt,
                 final Instant deletedAt,
                 final TypePerson type,
                 final String taxIdNumber,
                 final String name,
                 final Instant keyOwnerShipDate
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.type = type;
        this.taxIdNumber = TaxIdNumber.from(type, taxIdNumber);
        this.name = name;
        this.keyOwnerShipDate = keyOwnerShipDate;
    }
}
