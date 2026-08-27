package com.E3N.pix.domain.valueObject.branch;

import com.E3N.pix.domain.ValueObject;
import com.E3N.pix.domain.validation.Notification;
import org.apache.commons.lang3.StringUtils;

public class Branch extends ValueObject {

    private String branch;
    private Notification notification;

    private Branch(String branch) {
        this.branch = StringUtils.leftPad(branch, 4, "0");
        validate();
    }

    public static Branch getInstance(final String branch) {
        return new Branch(branch);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new BranchValidator(this).validate();
        if (this.notification.hasError()) {
            this.branch = null;
        }
    }

    public String getBranch() {
        return branch;
    }

    public Notification getNotification() {
        return notification;
    }
}
