package com.E3N.pix.domain.valueObject.name;


import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.Notification;

public class Name extends Entity {

    private String name;
    private Notification notification;

    private Name(String name) {
        this.name = name;
        validate();
    }

    public static Name getInstance(final String name){
        return new Name(name);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new NameValidator(this).validate();
        if (this.notification.hasError()){
            this.name = null;
        } else {
            this.notification = null;
        }
    }
    public String getName() {
        return name;
    }

    public Notification getNotification() {
        return notification;
    }
}
