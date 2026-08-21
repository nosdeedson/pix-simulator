package com.E3N.pix.domain.valueObject.participant;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.Notification;

import java.util.regex.Pattern;

public final class Participant extends Entity {


    private String participant;
    private Notification notification;

    private Participant(final String participant) {
        this.participant = participant;
        this.validate();
    }

    public static Participant getInstance(String participant){
        return new Participant(participant);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new ParticipantValidator(this).validate();
        if (notification.hasError()){
            this.participant = null;
        } else {
            this.notification = null;
        }
    }

    public String getParticipant() {
        return participant;
    }

    public Notification getNotification() {
        return notification;
    }
}
