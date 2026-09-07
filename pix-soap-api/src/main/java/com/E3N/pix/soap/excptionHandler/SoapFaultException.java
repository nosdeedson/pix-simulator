package com.E3N.pix.soap.excptionHandler;

import com.E3N.pix.domain.validation.Notification;

public class SoapFaultException extends RuntimeException {
    private final Notification notification;

    public SoapFaultException(String message, Notification notification) {
        super(message);
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
}
