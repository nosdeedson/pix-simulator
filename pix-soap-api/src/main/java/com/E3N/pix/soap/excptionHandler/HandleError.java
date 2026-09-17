package com.E3N.pix.soap.excptionHandler;

import com.E3N.pix.domain.validation.Notification;

public abstract class HandleError {

    public static SoapFaultException handleError(Exception e) {
        if (e instanceof SoapFaultException) {
            return (SoapFaultException) e;
        }
        Notification notification = Notification.create("Could not process the request.",
                500, "Unknow error while processing the request.");
        return new SoapFaultException("Failer", notification);
    }
}
