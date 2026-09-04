package com.E3N.pix.domain.modules.owner.entryKey;

public enum Reason {
    ACCOUNT_CLOSURE(1),
    BRANCH_TRANSFER(2),
    FRAUD(3),
    PARTICIPANT_EXCLUSION(4),
    RFB_VALIDATION(5),
    RECONCILIATION(6),
    USER_REQUESTED(7);

    private final int pin;

    Reason(int pin) {
        this.pin = pin;
    }
}
