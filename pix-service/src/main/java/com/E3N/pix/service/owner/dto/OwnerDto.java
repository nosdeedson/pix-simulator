package com.E3N.pix.service.owner.dto;

import com.E3N.pix.domain.modules.owner.owner.TypePerson;

public record OwnerDto(
        String name,
        String tradeName,
        String taxIdNumber,
        TypePerson typePerson,
        String openClaimCreationDate,
        AccountDto account
) {
    public static class Builder {
        String name;
        String taxIdNumber;
        TypePerson typePerson;
        String tradeName;
        String openClaimCreationDate;
        AccountDto accountDto;

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

        public Builder account(AccountDto accountDto) {
            this.accountDto = accountDto;
            return this;
        }

        public OwnerDto build() {
            return new OwnerDto(name, tradeName, taxIdNumber, typePerson, openClaimCreationDate, accountDto);
        }
    }

    @Override
    public String toString() {
        return "OwnerDto{" +
                "name='" + name + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", taxIdNumber='" + taxIdNumber + '\'' +
                ", typePerson=" + typePerson +
                ", openClaimCreationDate='" + openClaimCreationDate + '\'' +
                ", account=" + account +
                '}';
    }
}
