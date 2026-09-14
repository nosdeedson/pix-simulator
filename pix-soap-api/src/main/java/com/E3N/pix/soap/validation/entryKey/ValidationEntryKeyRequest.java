package com.E3N.pix.soap.validation.entryKey;

import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.soap.excptionHandler.SoapFaultException;

public abstract class ValidationEntryKeyRequest {

    public static void validateHeaderGetKey(final String piRequestingParticipant, final String piPayerId, final String piEndToEndId) {
        if (piEndToEndId == null || piPayerId == null || piRequestingParticipant == null) {
            Notification notification = Notification.create("Bad Request", 400, "The required Headers was not provided.");
            throw new SoapFaultException("Headers required", notification);
        }
    }
}
