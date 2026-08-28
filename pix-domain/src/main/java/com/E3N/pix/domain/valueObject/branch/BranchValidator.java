package com.E3N.pix.domain.valueObject.branch;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class BranchValidator extends Validator {

    private final static String PROPERTY = "Account.branch";
    private final Branch branch;

    public BranchValidator(Branch branch) {
        super(branch.getNotification());
        this.branch = branch;
    }

    @Override
    public ValidationHandler validate() {
        if (this.branch.getBranch() == null) {
            validationHandler().append("Branch is null", branch.getBranch(), PROPERTY);
        } else if (!this.branch.getBranch().matches("^(?!0+$)\\d{1,4}$")) {
            validationHandler().append("Branch is invalid.", branch.getBranch(), PROPERTY);
        }
        return validationHandler();
    }
}
