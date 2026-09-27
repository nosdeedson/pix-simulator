package com.E3N.pix.domain.modules.claim;

public enum StatusClaim {
    CANCELLED(1),
    CONFIRMED(2),
    COMPLETED(3),
    OPEN(4),
    WAITING_RESOLUTION(5);

    final private int pin;

    StatusClaim(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
