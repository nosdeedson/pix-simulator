package com.E3N.soap.mapper.mocks.claim;

import com.E3N.pix.soap.contract.*;
import com.E3N.shared.utils.DateHelper;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomCpfMock;
import com.E3N.test.Owner.RandomParticipant;
import com.E3N.test.Owner.RandomValidName;

import java.time.Instant;
import java.util.UUID;

public abstract class ClaimRequestMock {

    private static AccountType accountTypeMock(){
        var acc = new AccountType();
        acc.setAccountNumber(RandomAccountMock.randomAccountNumber());
        acc.setAccountType(AccountTypeEnum.CACC);
        acc.setBranch(RandomAccountMock.randomBranch());
        acc.setParticipant(RandomParticipant.getParticipant());
        acc.setOpeningDate(DateHelper.fromInstant(Instant.now()));
        return acc;
    }

    private static OwnerType ownerTypeMock(){
        var owner = new OwnerType();
        owner.setName(RandomValidName.randomValidName());
        owner.setTaxIdNumber(RandomCpfMock.getRandomCFP());
        owner.setType(OwnerTypeEnum.NATURAL_PERSON);
        return owner;
    }

    private static ClaimType claimTypeMock(String key, KeyType keyType, TypeClaims typeClaims){
        var claimType = new ClaimType();
        claimType.setClaimerAccount(accountTypeMock());
        claimType.setClaimer(ownerTypeMock());
        claimType.setKey(key);
        claimType.setKeyType(keyType);
        claimType.setType(typeClaims);
        return claimType;
    }

    public static CreateClaimRequest createClaimRequestMock(
            String key, KeyType keyType, TypeClaims typeClaims
    ){
        var request = new CreateClaimRequest();
        request.setClaim(claimTypeMock(key, keyType, typeClaims));
        request.setSignature(UUID.randomUUID().toString());
        return request;
    }
}
