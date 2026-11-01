package com.E3N.pix.service.claim.enums;

public enum ClaimOperationReason {

    USER_REQUESTED(1),
    ACCOUNT_CLOSURE(2),
    FRAUD(3),
    DEFAULT_OPERATION(4),
    RECONCILIATION(5),
    RFB_VALIDATION(6),
    PARTICIPANT_EXCLUSION(7);

    private final int pin;

    ClaimOperationReason(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
