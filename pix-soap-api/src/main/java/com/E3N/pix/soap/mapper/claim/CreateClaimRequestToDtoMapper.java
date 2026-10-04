package com.E3N.pix.soap.mapper.claim;

import com.E3N.pix.application.claim.dto.ClaimDto;
import com.E3N.pix.application.claim.dto.ClaimerAccountDto;
import com.E3N.pix.application.claim.dto.ClaimerDto;
import com.E3N.pix.domain.modules.claim.TypeClaim;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.soap.contract.AccountType;
import com.E3N.pix.soap.contract.ClaimType;
import com.E3N.pix.soap.contract.CreateClaimRequest;
import com.E3N.pix.soap.contract.OwnerType;
import com.E3N.shared.utils.DateHelper;

public abstract class CreateClaimRequestToDtoMapper {

    private static ClaimerAccountDto toClaimerAccount(AccountType acc){
        return new ClaimerAccountDto(
                acc.getParticipant(),
                acc.getBranch(),
                acc.getParticipant(),
                com.E3N.pix.domain.modules.ownership.account.AccountType.valueOf(acc.getAccountType().name()),
                DateHelper.fromGregorianCalendar(acc.getOpeningDate())
        );
    }

    private static ClaimerDto toClaimerDto(OwnerType ownerType){
        return new ClaimerDto(
                TypePerson.valueOf(ownerType.getType().name()),
                ownerType.getTaxIdNumber(),
                ownerType.getName(),
                ownerType.getTradeName()
        );
    }

    public static ClaimDto from(ClaimType claim) {
        return new ClaimDto(
                TypeClaim.valueOf(claim.getType().name()),
                claim.getKey(),
                TypeKey.valueOf(claim.getKeyType().name()),
                toClaimerAccount(claim.getClaimerAccount()),
                toClaimerDto(claim.getClaimer())
        );
    }

}
