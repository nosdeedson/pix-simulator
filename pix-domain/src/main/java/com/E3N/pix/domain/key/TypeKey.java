package com.E3N.pix.domain.key;


public enum TypeKey {
    PHONE(1),
    CPF(2),
    CNPJ(3),
    EMAIL(4),
    EVP(5);
    private final int pin;

    TypeKey(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }
}
