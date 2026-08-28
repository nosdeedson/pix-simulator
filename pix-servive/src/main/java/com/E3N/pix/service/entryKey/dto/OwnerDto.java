package com.E3N.pix.service.entryKey.dto;

import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;

public record OwnerDto(
        String keyOwnershipDate,
        String name,
        String taxIdNumber,
        TypePerson typePerson,
        String tradeName,
        String openClaimCreationDate
) {
    public static class Builder {
        String keyOwnershipDate;
        String name;
        String taxIdNumber;
        TypePerson typePerson;
        String tradeName;
        String openClaimCreationDate;

        public Builder keyOwnershipDate(String keyOwnershipDate) {
            this.keyOwnershipDate = keyOwnershipDate;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder taxIdNumber(String taxIdNumber) {
            this.taxIdNumber = taxIdNumber;
            return this;
        }

        public Builder typePerson(TypePerson typePerson) {
            this.typePerson = typePerson;
            return this;
        }

        public Builder tradeName(String tradeName) {
            this.tradeName = tradeName;
            return this;
        }

        public Builder openClaimCreationDate(String openClaimCreationDate) {
            this.openClaimCreationDate = openClaimCreationDate;
            return this;
        }

        public OwnerDto build() {
            return new OwnerDto(keyOwnershipDate, name, taxIdNumber, typePerson, tradeName, openClaimCreationDate);
        }
    }

    public Owner toEntity() {
        if (TypePerson.LEGAL_PERSON.equals(this.typePerson)) {
            return Owner.getInstanceLegalPerson(
                    this.keyOwnershipDate,
                    this.name,
                    this.taxIdNumber,
                    this.tradeName,
                    this.typePerson
            );
        }
        return Owner.getInstanceNaturalPerson(
                this.keyOwnershipDate,
                this.name,
                this.taxIdNumber,
                this.typePerson
        );
    }
}
