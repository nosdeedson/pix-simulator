package com.E3N.pix.domain.modules.owner.account;

public enum AccountType {
    CACC(1), // CONTA CORRENTE
    TRAN(2), //CONTA PAGAMENTO
    SLRY(3), // CONTA POUPANÇA
    SVGS(4),
    OTHR(5);

    AccountType(int pin) {
        this.pin = pin;
    }

    private final int pin;

    public int getPin() {
        return pin;
    }
}
