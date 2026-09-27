package com.E3N.pix.domain.modules.claim;

public enum TypeClaim {
    OWNERSHIP(1), PORTABILITY(2);

    final private int pin;

    TypeClaim(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
