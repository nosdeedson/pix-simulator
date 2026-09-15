package com.E3N.pix.domain.modules.ownership.account;

import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.pix.domain.valueObject.key.TypeKey;

import java.util.Optional;

public class AccountValidator extends Validator {
    private final static String PROPERTY = "Account.";
    private final Account account;

    protected AccountValidator(Account account) {
        super(account.getNotification());
        this.account = account;
    }

    @Override
    public ValidationHandler validate() {
        if (this.account.getEntryKeys() == null) {
            validationHandler().append("At least one EntryKey should be informed", null, "Account.entryKey");
        }
        if (this.account.getEntryKeys() != null && !this.account.getEntryKeys().isEmpty()) {
            var entryKey = this.account.getEntryKeys().getLast();
            if (entryKey.getNotification() != null && entryKey.getNotification().hasError()) {
                validationHandler().relateNotificationToMe(PROPERTY,
                        entryKey.getNotification().getViolations()
                );
            }
        }
        if (this.account.getBranch().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getBranch().getNotification().getViolations()
            );
        }
        if (this.account.getNumber().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getNumber().getNotification().getViolations()
            );
        }
        if (this.account.getOpeningDate() == null) {
            validationHandler().append("OpeningDate is required.", null, "Account.openingDate");
        }
        if (this.account.getParticipant().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getParticipant().getNotification().getViolations()
            );
        }
        if (this.account.getType() == null) {
            validationHandler().append("Account Type is required.", null, "Account.type");
        }
        return this.account.getNotification();
    }

    public ValidationHandler validateUpdate(UpdateEntryKeyDto dto) {
        var accountDto = dto.accountDto();
        if (!accountDto.participant().equals(this.account.getParticipant().getParticipant())) {
            validationHandler().append("Participants different, Open a claim or portability.", accountDto.participant(), "Account.participant");
        }
        Optional<EntryKey> key = this.account.getEntryKeys().stream()
                .filter(it -> it.getKey().getKey().equals(dto.key()))
                .findAny();
        if (key.isEmpty()) {
            validationHandler().append("There is no key that matches the key passed", dto.key(), "Account.entryKey");
        } else {
            if (key.get().isInvalidUpdate(dto.reason())) {
                validationHandler().append("The given reason is not allowed to update the kind of Key", dto.reason().name(), "Account.key");
            }
        }
        return this.account.getNotification();
    }

    private boolean isInvalidUpdate(Reason reason, TypeKey typeKey) {
        if (TypeKey.EVP.equals(typeKey)
                && !(reason.equals(Reason.BRANCH_TRANSFER)
                || reason.equals(Reason.RECONCILIATION)
                || reason.equals(Reason.RFB_VALIDATION))
        ) {
            return true;
        }
        return !TypeKey.EVP.equals(typeKey)
                && !(
                reason.equals(Reason.BRANCH_TRANSFER)
                        || reason.equals(Reason.USER_REQUESTED)
                        || reason.equals(Reason.RECONCILIATION)
                        || reason.equals(Reason.RFB_VALIDATION)
        );
    }
}
