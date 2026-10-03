package com.E3N.pix.domain.modules.claim;

import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class ClaimValidator extends Validator {

    private final Claim claim;

    public ClaimValidator(Claim claim) {
        super(claim.getNotification());
        this.claim = claim;
    }

    @Override
    public ValidationHandler validate() {
        if (claim.getTypeKeyClaimed().equals(TypeKey.EVP)){
            validationHandler().append("Is not allowed to create a claim for a key if type EVP", claim.getKeyClaimed(), "key");
        }
        if (claim.getTaxIdNumberClaimer().getNotification().hasError()){
            validationHandler().relateNotificationToMe("taxIdNumber", claim.getTaxIdNumberClaimer().getNotification().getViolations());
        }
        if (validKeyForPortability(claim.getTypeKeyClaimed()) && claim.getType().equals(TypeClaim.OWNERSHIP)){
            validationHandler().append("This kind of Key must open a portability", claim.getKeyClaimed(), "key");
        }
        if (
                claim.getTypeKeyClaimed().equals(TypeKey.CNPJ)
                && !claim.getKeyClaimed().equals(claim.getTaxIdNumberClaimer().getTaxIdNumber())
        ){
            validationHandler().append("This kind of portability need to have key equals to taxIdNumber", claim.getKeyClaimed(), "key");
        }
        if (
                claim.getTypeKeyClaimed().equals(TypeKey.CPF)
                        && !claim.getKeyClaimed().equals(claim.getTaxIdNumberClaimer().getTaxIdNumber())
        ){
            validationHandler().append("This kind of portability need to have key equals to taxIdNumber", claim.getKeyClaimed(), "key");
        }
        if (!claim.getKeyClaimed().equals(claim.getTaxIdNumberClaimer().getTaxIdNumber())
            && claim.getTypeKeyClaimed().equals(TypeKey.CPF)
        ){
            validationHandler().append("The type of key must be CPF", claim.getKeyClaimed(), "key");
        }
        return validationHandler();
    }

    public boolean validKeyForPortability(TypeKey typeKey){
        return !TypeKey.EVP.equals(typeKey);
    }
}
