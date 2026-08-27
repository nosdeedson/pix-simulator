package com.E3N.pix.domain.valueObject.name;


import com.E3N.pix.domain.ValueObject;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;

public class Name extends ValueObject {

    private String name;
    private TypePerson type;
    private Notification notification;


    private Name(final String name, final TypePerson type) {
        this.name = name;
        this.type = type;
        validate();
    }

    public static Name getInstance(final String name, final TypePerson type) {
        return new Name(name, type);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new NameValidator(this).validate();
        if (this.notification.hasError()) {
            this.name = null;
        }
    }

    public String getName() {
        return name;
    }

    public Notification getNotification() {
        return notification;
    }

    public TypePerson getType() {
        return type;
    }
}
