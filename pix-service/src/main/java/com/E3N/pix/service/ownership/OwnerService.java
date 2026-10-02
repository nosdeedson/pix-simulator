package com.E3N.pix.service.ownership;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.service.ownership.dto.OwnerDto;

import java.util.Optional;

public class OwnerService {

    public Owner create(final OwnerDto dto) {
        return Owner.getInstance(
                dto.name(),
                dto.tradeName(),
                dto.taxIdNumber(),
                dto.typePerson(),
                dto.account().toEntity()
        );
    }

    public Notification validateKeyExistence(final Owner owner, final OwnerDto dto) {
        Notification notification = Notification.create();
        if (owner.getTaxIdNumber().getTaxIdNumber().equals(dto.taxIdNumber())) {
            Optional<Account> sameParticipantAndAccountNumber = owner.getAccounts().stream()
                    .filter(it -> it.sameParticipant(dto.account().participant(), dto.account().accountNumber()))
                    .findFirst();
            if (sameParticipantAndAccountNumber.isPresent()) {
                // OK TESTED
                notification.append("Key already exists.", dto.account().entryKeyDto().key(), "Owner.Key");
            }
            Optional<Account> participantDifferent = owner.getAccounts().stream()
                    .filter(it -> !it.getParticipant().getParticipant().equals(dto.account().participant()))
                    .findFirst();
            if (participantDifferent.isPresent()) {
                notification.append(
                        "Key is registered in another bank, create a portability or claims ownership.",
                        dto.account().entryKeyDto().key(), "Owner.key"
                );
            }
        }
        return notification;
    }

}
