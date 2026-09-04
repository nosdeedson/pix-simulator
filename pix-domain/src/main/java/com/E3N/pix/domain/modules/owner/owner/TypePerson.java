package com.E3N.pix.domain.modules.owner.owner;

public enum TypePerson {
    NATURAL_PERSON(0), LEGAL_PERSON(1);
    private int pin;

    TypePerson(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
