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
            validationHandler().append("Is not allowed to create a claim for a key if type is EVP", claim.getKeyClaimed(), "key");
        }
        if (claim.getTaxIdNumberClaimer().getNotification().hasError()){
            validationHandler().relateNotificationToMe("taxIdNumber", claim.getTaxIdNumberClaimer().getNotification().getViolations());
        }
        if (
                (claim.getTypeKeyClaimed().equals(TypeKey.CNPJ) || claim.getTypeKeyClaimed().equals(TypeKey.CPF))
                    && !claim.getKeyClaimed().equals(claim.getTaxIdNumberClaimer().getTaxIdNumber())
        ){
            validationHandler().append("This kind of portability need to have key equals to taxIdNumber", claim.getKeyClaimed(), "key");
        }
        if (!claim.getClaimerParticipant().equals(claim.getDonorParticipant())
                && TypeClaim.OWNERSHIP.equals(claim.getType())
                && !TypeKey.PHONE.equals(claim.getTypeKeyClaimed())
        ){
            validationHandler().append("The type of claim must be Portability", claim.getKeyClaimed(), "Claim.key");
        }
        return validationHandler();
    }

}
