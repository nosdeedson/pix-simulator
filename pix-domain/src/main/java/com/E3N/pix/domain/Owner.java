package com.E3N.pix.domain;

import java.time.Instant;

public class Owner {
    private final TypePerson type;
    private final String taxIdNumber; // cpf/cnpj
    private final String name;
    private final Instant creationDate;
    private final Instant keyOwnerShipDate;
    private Instant openClaimCreateionDate;

    public Owner(TypePerson type, String taxIdNumber, String name, Instant creationDate, Instant keyOwnerShipDate) {
        this.type = type;
        this.taxIdNumber = taxIdNumber;
        this.name = name;
        this.creationDate = creationDate;
        this.keyOwnerShipDate = keyOwnerShipDate;
    }
}
