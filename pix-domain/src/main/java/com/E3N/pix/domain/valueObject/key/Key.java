package com.E3N.pix.domain.valueObject.key;

import com.E3N.pix.domain.Either;
import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.validation.ValidationHandler;

@SuppressWarnings("all")
public class Key extends Entity {
    private String key;
    private TypeKey type;
    private Notification notification;

    private Key(String key, TypeKey type) {
        this.key = key;
        this.type = type;
        this.validate();
    }

    public static Key getInstance(final String key, final TypeKey type){
        return new Key(key, type);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new KeyValidator(this).validate();
        if (this.notification.hasError()){
            this.key = null;
            this.type = null;
        } else {
            this.notification = null;
        }
    }

    public TypeKey getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public Notification getNotification() {
        return notification;
    }
}
